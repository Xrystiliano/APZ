CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);


CREATE TABLE locks (
    lock_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    device_serial_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) DEFAULT 'offline' NOT NULL,
    is_locked BOOLEAN DEFAULT true NOT NULL,
    last_heartbeat_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    timezone VARCHAR(255),
    secret_key VARCHAR(255),
    CONSTRAINT fk_locks_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE INDEX idx_locks_user_id ON locks(user_id);


CREATE TABLE accesskeys (
    access_key_id UUID PRIMARY KEY,
    lock_id UUID NOT NULL,
    created_by_user_id UUID NOT NULL,
    access_token VARCHAR(255) NOT NULL UNIQUE,
    valid_from TIMESTAMP WITH TIME ZONE NOT NULL,
    valid_until TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT true NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT fk_accesskeys_user FOREIGN KEY (created_by_user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_accesskeys_lock FOREIGN KEY (lock_id) REFERENCES locks (lock_id) ON DELETE CASCADE
);

CREATE INDEX idx_accesskeys_access_token ON accesskeys(access_token);
CREATE INDEX idx_accesskeys_lock_id ON accesskeys(lock_id);


CREATE TABLE lock_roles (
    access_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lock_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT (now() AT TIME ZONE 'utc') NOT NULL,
    CONSTRAINT fk_roles_lock FOREIGN KEY (lock_id) REFERENCES locks (lock_id) ON DELETE CASCADE,
    CONSTRAINT fk_roles_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    -- Cleaned up Unique Constraint: Only one role per user per lock
    CONSTRAINT uq_lock_roles_user_lock UNIQUE (user_id, lock_id)
);

CREATE INDEX idx_lock_roles_lock_id ON lock_roles(lock_id);
CREATE INDEX idx_lock_roles_user_id ON lock_roles(user_id);


CREATE TABLE activitylogs (
    log_id BIGSERIAL PRIMARY KEY,
    lock_id UUID NOT NULL,
    action_type VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    actor_user_id UUID,
    actor_key_id UUID,
    details VARCHAR(255),
    CONSTRAINT fk_logs_lock FOREIGN KEY (lock_id) REFERENCES locks (lock_id) ON DELETE CASCADE,
    CONSTRAINT fk_logs_user FOREIGN KEY (actor_user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    CONSTRAINT fk_logs_key FOREIGN KEY (actor_key_id) REFERENCES accesskeys (access_key_id) ON DELETE SET NULL
);

CREATE INDEX idx_activitylogs_lock_timestamp ON activitylogs(lock_id, timestamp DESC);