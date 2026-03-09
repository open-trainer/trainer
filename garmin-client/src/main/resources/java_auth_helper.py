#!/usr/bin/env python3
"""🔐 Garmin Connect Login and Token Management Script.
=====================================================

This script handles:
- Login with email/password
- MFA (Multi-Factor Authentication) support
- Token storage and validation
- Token location information

Dependencies:
pip3 install garth requests

Environment Variables (optional):
export EMAIL=<your garmin email address>
export PASSWORD=<your garmin password>
export GARMINTOKENS=<path to token storage>
"""

import os
import sys
from getpass import getpass
from pathlib import Path
from typing import Optional

from garth.exc import GarthException, GarthHTTPError

from garminconnect import (
    Garmin,
    GarminConnectAuthenticationError,
    GarminConnectConnectionError,
)


def get_credentials():
    """Get email and password from environment or user input."""
    email = os.getenv("EMAIL")
    password = os.getenv("PASSWORD")

    if not email:
        email = input("Login email: ").strip()
    if not password:
        password = getpass("Enter password: ")

    return email, password


def login_and_save_tokens(tokenstore_path: Path) -> Optional[Garmin]:
    """Login with credentials and save tokens."""
    print("\n📝 Please enter your Garmin Connect credentials:")

    while True:
        try:
            # Get credentials
            email, password = get_credentials()

            print("\n🔐 Logging in...")
            garmin = Garmin(
                email=email, password=password, is_cn=False, return_on_mfa=True
            )
            result1, result2 = garmin.login()

            if result1 == "needs_mfa":
                print("\n🔒 MFA (Multi-Factor Authentication) required.")
                mfa_code = input("Please enter your MFA code: ").strip()

                try:
                    print("Verifying MFA code...")
                    garmin.resume_login(result2, mfa_code)
                    print("✅ MFA verification successful!")

                except GarthHTTPError as garth_error:
                    error_str = str(garth_error)
                    if "429" in error_str or "Too Many Requests" in error_str:
                        print("❌ Rate limit exceeded. Please wait before trying again.")
                        sys.exit(1)
                    elif "401" in error_str or "403" in error_str:
                        print("❌ Invalid MFA code. Please try again.")
                        continue
                    else:
                        print(f"❌ MFA error: {garth_error}")
                        sys.exit(1)

                except GarthException as e:
                    print(f"❌ MFA error: {e}")
                    continue

            # Save tokens for future use
            print(f"\n💾 Saving tokens to: {tokenstore_path}")
            tokenstore_path.mkdir(parents=True, exist_ok=True)
            garmin.garth.dump(str(tokenstore_path))

            # Verify tokens were saved
            token_files = list(tokenstore_path.glob("*.json"))
            if token_files:
                print(f"✅ Tokens saved successfully! ({len(token_files)} token file(s))")
            else:
                print("⚠️  Warning: Token files not found after save.")

            return garmin

        except GarminConnectAuthenticationError as e:
            print(f"❌ Authentication failed: {e}")
            print("Please check your email and password and try again.\n")
            continue

        except (GarminConnectConnectionError, GarthHTTPError) as e:
            print(f"❌ Connection error: {e}")
            return None

        except KeyboardInterrupt:
            print("\n\n⚠️  Login cancelled by user.")
            return None

        except Exception as e:
            print(f"❌ Unexpected error: {e}")
            return None


def main():
    """Main function to handle login and token management."""
    # Configure token storage
    tokenstore = os.getenv("GARMINTOKENS", "~/.garminconnect")
    tokenstore_path = Path(tokenstore).expanduser()

    print("=" * 60)
    print("🔐 Garmin Connect Login & Token Management")
    print("=" * 60)
    print(f"\n📁 Token storage location: {tokenstore_path}")

    # Check if token files exist
    existing_tokens = False
    if tokenstore_path.exists():
        token_files = list(tokenstore_path.glob("*.json"))
        if token_files:
            existing_tokens = True
            print(f"✅ Found existing tokens ({len(token_files)} file(s))")
        else:
            print("ℹ️  Token directory exists but no token files found")
    else:
        print("ℹ️  No token directory found - will create one")

    # Try to login with stored tokens first
    if existing_tokens:
        print("\n🔄 Attempting to login with stored tokens...")
        try:
            garmin = Garmin()
            garmin.login(str(tokenstore_path))
            print("✅ Successfully logged in using stored tokens!")
            print("\n🎉 Login successful! Tokens are valid and ready to use.")
            print(f"📁 Tokens stored at: {tokenstore_path}")
            return

        except FileNotFoundError:
            print("⚠️  Token files not found. Need to login with credentials.")
        except (GarthHTTPError, GarminConnectAuthenticationError) as e:
            print(f"⚠️  Stored tokens are invalid or expired: {e}")
            print("Need to login with credentials to refresh tokens.")
        except GarminConnectConnectionError as e:
            print(f"❌ Connection error: {e}")
            return
        except Exception as e:
            print(f"⚠️  Error loading tokens: {e}")
            print("Need to login with credentials.")

    # Login with credentials and save tokens
    garmin = login_and_save_tokens(tokenstore_path)

    if garmin:
        print("\n" + "=" * 60)
        print("🎉 Login successful!")
        print("=" * 60)
        print(f"✅ Tokens saved to: {tokenstore_path}")
        print("\n💡 You can now use these tokens in other scripts.")
        print("   Set GARMINTOKENS environment variable to use a different location.")
    else:
        print("\n❌ Login failed. Please try again.")
        sys.exit(1)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n⚠️  Interrupted by user. Exiting...")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Fatal error: {e}")
        sys.exit(1)
