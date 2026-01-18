#!/usr/bin/env python3
"""
Garmin Connect Authentication Helper for Java Integration

This script can be called from Java to handle Garmin Connect authentication
and return tokens as JSON.

Usage:
    python3 java_auth_helper.py <email> <password>
    python3 java_auth_helper.py load [tokenstore_path]
    python3 java_auth_helper.py refresh <refresh_token>
"""

import sys
import json
import os
from pathlib import Path

try:
    from garminconnect import Garmin
    from garth.exc import GarthException, GarthHTTPError
except ImportError:
    print(json.dumps({
        "error": "Missing dependencies. Install: pip install garminconnect garth",
        "status": "error"
    }), file=sys.stderr)
    sys.exit(1)


def authenticate(email: str, password: str, is_cn: bool = False):
    """Authenticate with email/password and return tokens as JSON."""
    try:
        garmin = Garmin(email=email, password=password, is_cn=is_cn, return_on_mfa=True)
        result1, result2 = garmin.login()
        
        if result1 == "needs_mfa":
            # Return MFA required status
            return json.dumps({
                "status": "needs_mfa",
                "client_state": result2,
                "error": None
            })
        
        # Extract tokens
        oauth1_token = None
        oauth1_secret = None
        oauth2_access_token = None
        oauth2_refresh_token = None
        
        if garmin.garth.oauth1_token:
            oauth1_token = garmin.garth.oauth1_token.token
            oauth1_secret = garmin.garth.oauth1_token.token_secret
        
        if garmin.garth.oauth2_token:
            oauth2_access_token = garmin.garth.oauth2_token.access_token
            oauth2_refresh_token = garmin.garth.oauth2_token.refresh_token
        
        # Save tokens for future use
        tokenstore = os.getenv("GARMINTOKENS", str(Path.home() / ".garminconnect"))
        garmin.garth.dump(tokenstore)
        
        # Get user profile info
        display_name = garmin.display_name
        full_name = garmin.full_name
        
        return json.dumps({
            "status": "success",
            "oauth1_token": oauth1_token,
            "oauth1_secret": oauth1_secret,
            "oauth2_access_token": oauth2_access_token,
            "oauth2_refresh_token": oauth2_refresh_token,
            "display_name": display_name,
            "full_name": full_name,
            "error": None
        })
        
    except Exception as e:
        return json.dumps({
            "status": "error",
            "error": str(e),
            "oauth1_token": None,
            "oauth1_secret": None,
            "oauth2_access_token": None,
            "oauth2_refresh_token": None
        })


def resume_login(client_state: str, mfa_code: str, is_cn: bool = False):
    """Resume login after MFA."""
    try:
        import json as json_lib
        # client_state might be a string representation of dict
        if isinstance(client_state, str):
            try:
                client_state = json_lib.loads(client_state)
            except:
                pass
        
        garmin = Garmin(is_cn=is_cn)
        garmin.resume_login(client_state, mfa_code)
        
        # Extract tokens
        oauth1_token = None
        oauth1_secret = None
        oauth2_access_token = None
        oauth2_refresh_token = None
        
        if garmin.garth.oauth1_token:
            oauth1_token = garmin.garth.oauth1_token.token
            oauth1_secret = garmin.garth.oauth1_token.token_secret
        
        if garmin.garth.oauth2_token:
            oauth2_access_token = garmin.garth.oauth2_token.access_token
            oauth2_refresh_token = garmin.garth.oauth2_token.refresh_token
        
        # Save tokens
        tokenstore = os.getenv("GARMINTOKENS", str(Path.home() / ".garminconnect"))
        garmin.garth.dump(tokenstore)
        
        display_name = garmin.display_name
        full_name = garmin.full_name
        
        return json.dumps({
            "status": "success",
            "oauth1_token": oauth1_token,
            "oauth1_secret": oauth1_secret,
            "oauth2_access_token": oauth2_access_token,
            "oauth2_refresh_token": oauth2_refresh_token,
            "display_name": display_name,
            "full_name": full_name,
            "error": None
        })
        
    except Exception as e:
        return json.dumps({
            "status": "error",
            "error": str(e),
            "oauth1_token": None,
            "oauth1_secret": None,
            "oauth2_access_token": None,
            "oauth2_refresh_token": None
        })


def load_tokens(tokenstore: str = None):
    """Load tokens from stored location."""
    try:
        if not tokenstore:
            tokenstore = os.getenv("GARMINTOKENS", str(Path.home() / ".garminconnect"))
        
        garmin = Garmin()
        garmin.login(tokenstore)
        
        # Extract tokens
        oauth1_token = None
        oauth1_secret = None
        oauth2_access_token = None
        oauth2_refresh_token = None
        
        if garmin.garth.oauth1_token:
            oauth1_token = garmin.garth.oauth1_token.token
            oauth1_secret = garmin.garth.oauth1_token.token_secret
        
        if garmin.garth.oauth2_token:
            oauth2_access_token = garmin.garth.oauth2_token.access_token
            oauth2_refresh_token = garmin.garth.oauth2_token.refresh_token
        
        display_name = garmin.display_name
        full_name = garmin.full_name
        
        return json.dumps({
            "status": "success",
            "oauth1_token": oauth1_token,
            "oauth1_secret": oauth1_secret,
            "oauth2_access_token": oauth2_access_token,
            "oauth2_refresh_token": oauth2_refresh_token,
            "display_name": display_name,
            "full_name": full_name,
            "error": None
        })
        
    except FileNotFoundError:
        return json.dumps({
            "status": "error",
            "error": "Token store not found. Please authenticate first.",
            "oauth1_token": None,
            "oauth1_secret": None,
            "oauth2_access_token": None,
            "oauth2_refresh_token": None
        })
    except Exception as e:
        return json.dumps({
            "status": "error",
            "error": str(e),
            "oauth1_token": None,
            "oauth1_secret": None,
            "oauth2_access_token": None,
            "oauth2_refresh_token": None
        })


def main():
    if len(sys.argv) < 2:
        print(json.dumps({
            "error": "Invalid arguments. Usage: authenticate <email> <password> | load [tokenstore] | resume <client_state> <mfa_code>",
            "status": "error"
        }), file=sys.stderr)
        sys.exit(1)
    
    command = sys.argv[1].lower()
    
    if command == "authenticate" and len(sys.argv) >= 4:
        email = sys.argv[2]
        password = sys.argv[3]
        is_cn = len(sys.argv) > 4 and sys.argv[4] == "cn"
        result = authenticate(email, password, is_cn)
        print(result)
        # Exit with error code if authentication failed
        response = json.loads(result)
        sys.exit(0 if response.get("status") in ("success", "needs_mfa") else 1)
        
    elif command == "load":
        tokenstore = sys.argv[2] if len(sys.argv) > 2 else None
        result = load_tokens(tokenstore)
        print(result)
        response = json.loads(result)
        sys.exit(0 if response.get("status") == "success" else 1)
        
    elif command == "resume" and len(sys.argv) >= 4:
        client_state = sys.argv[2]
        mfa_code = sys.argv[3]
        is_cn = len(sys.argv) > 4 and sys.argv[4] == "cn"
        result = resume_login(client_state, mfa_code, is_cn)
        print(result)
        response = json.loads(result)
        sys.exit(0 if response.get("status") == "success" else 1)
        
    else:
        # Backward compatibility: if first arg looks like email
        if "@" in sys.argv[1] and len(sys.argv) >= 3:
            email = sys.argv[1]
            password = sys.argv[2]
            result = authenticate(email, password)
            print(result)
            response = json.loads(result)
            sys.exit(0 if response.get("status") in ("success", "needs_mfa") else 1)
        else:
            print(json.dumps({
                "error": "Invalid arguments. Usage: authenticate <email> <password> | load [tokenstore] | resume <client_state> <mfa_code>",
                "status": "error"
            }), file=sys.stderr)
            sys.exit(1)


if __name__ == "__main__":
    main()
