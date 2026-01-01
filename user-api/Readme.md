# user-api

User API is responsible for user management.
Core principle: One user -> many identities (auth methods)
Model it as:

```User``` – business entity

```Identity / Credential``` – how the user authenticates

Data model:
```text
user
- id (UUID)
- status (ACTIVE, PENDING, BLOCKED)
- created_at
- updated_at
```
Identity (Authentication Method)
```text
identity
- id
- user_id
- type (EMAIL, PHONE, GOOGLE, APPLE, SSO)
- identifier (email, phone, provider_user_id)
- verified (boolean)
- created_at
```

Secret
```text
secret
- identity_id
- password_hash (nullable)
- salt
```

Verification
```text
verification
- identity_id
- type (EMAIL, PHONE)
- code
- expires_at
- attempts
```

## API:
* ```/api/v1/registration``` - Unified Registration Endpoint
Request:
```json
{
  "method": "EMAIL",
  "identifier": "user@example.com",
  "secret": "password123"
}
```
```json
 {
  "method": "PHONE",
  "identifier": "+998901234567"
}
```
Response:
```json
{
  "userId": "uuid",
  "status": "PENDING_VERIFICATION",
  "nextStep": "VERIFY_EMAIL"
}
```

## Flows:
### Email / Password Registration
1. Create User 
2. Create Identity(type=EMAIL)
3. Store password hash 
4. Send verification email 
5. Activate after verification

Pros
* Full control 
* Works everywhere
Cons 
* Password management 
* Higher security responsibility

### Phone (OTP)
1. Create User 
2. Create Identity(type=PHONE)
3. Send OTP 
4. Verify OTP → activate

# TODO
1. Add other registration methods
2. Detecting "Same user" Safely and merging
3. Registration flow