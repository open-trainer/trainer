-- V2: Support multiple registration options

CREATE TABLE IF NOT EXISTS user_registration_options (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    registration_type VARCHAR(50) NOT NULL,
    identifier VARCHAR(255) NOT NULL,
    secret TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (registration_type, identifier)
);

-- Migrating existing users from V1 if any (assuming they were login/password based)
-- We'll keep the columns in users table for now to avoid breaking things, but new data should go to user_registration_options

-- Add index
CREATE INDEX idx_user_registration_options_user_id ON user_registration_options(user_id);
CREATE INDEX idx_user_registration_options_lookup ON user_registration_options(registration_type, identifier);

-- Optional: Alter users table to make some columns nullable if they are now handled by registration options
-- ALTER TABLE users ALTER COLUMN password_hash DROP NOT NULL;
-- ALTER TABLE users ALTER COLUMN username DROP NOT NULL;
