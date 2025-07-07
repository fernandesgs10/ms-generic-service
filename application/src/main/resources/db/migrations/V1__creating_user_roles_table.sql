CREATE TABLE IF NOT EXISTS tb_roles (
    id VARCHAR(255) PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    nm_created VARCHAR(255) NOT NULL,
    dt_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nm_edited VARCHAR(255),
    dt_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id VARCHAR(255),
    permission VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_id, permission),
    FOREIGN KEY (role_id) REFERENCES tb_roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_users (
    id VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    doc VARCHAR(255),
    cellphone VARCHAR(255),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    is_send_email BOOLEAN NOT NULL DEFAULT FALSE,
    dt_birthday DATE,
    sms_token VARCHAR(255),
    nm_created VARCHAR(255) NOT NULL,
    dt_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nm_edited VARCHAR(255),
    dt_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(255),
    role_id VARCHAR(255),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES tb_roles(id) ON DELETE CASCADE
);