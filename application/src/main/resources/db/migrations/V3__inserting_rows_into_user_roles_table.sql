INSERT INTO tb_roles (id, role_name, nm_created, dt_created, nm_edited, dt_updated)
VALUES
    ('role_admin', 'ADMIN', 'admin_creator', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
    ('role_user', 'USER', 'user_creator', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserindo permissões para role_admin
INSERT INTO role_permissions (role_id, permission)
SELECT 'role_admin', 'READ_PRIVILEGES'
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions WHERE role_id = 'role_admin' AND permission = 'READ_PRIVILEGES'
);

INSERT INTO role_permissions (role_id, permission)
SELECT 'role_admin', 'WRITE_PRIVILEGES'
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions WHERE role_id = 'role_admin' AND permission = 'WRITE_PRIVILEGES'
);

INSERT INTO role_permissions (role_id, permission)
SELECT 'role_admin', 'DELETE_PRIVILEGES'
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions WHERE role_id = 'role_admin' AND permission = 'DELETE_PRIVILEGES'
);

-- Inserindo permissões para role_user
INSERT INTO role_permissions (role_id, permission)
SELECT 'role_user', 'READ_PRIVILEGES'
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions WHERE role_id = 'role_user' AND permission = 'READ_PRIVILEGES'
);

INSERT INTO tb_users (id, password, email, first_name, last_name, doc, cellphone, status, is_send_email, dt_birthday, sms_token, nm_created, dt_created, nm_edited, dt_updated)
VALUES
    ('user_john', 'am9obg==', 'john.doe@example.com', 'John', 'Doe', '123456789', '999999999', TRUE, FALSE, '1990-01-01', 'token123', 'admin_creator', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
    ('user_jane', 'amFuZQ==', 'jane.doe@example.com', 'Jane', 'Doe', '987654321', '888888888', TRUE, TRUE, '1992-02-02', 'token456', 'admin_creator', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;


-- Inserindo relação entre user_john e role_admin
INSERT INTO user_roles (user_id, role_id)
SELECT 'user_john', 'role_admin'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles WHERE user_id = 'user_john' AND role_id = 'role_admin'
);

-- Inserindo relação entre user_jane e role_user
INSERT INTO user_roles (user_id, role_id)
SELECT 'user_jane', 'role_user'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles WHERE user_id = 'user_jane' AND role_id = 'role_user'
);
