CREATE TABLE IF NOT EXISTS tb_account_payment (
    id BIGSERIAL PRIMARY KEY,
    due_date DATE NOT NULL,
    payment_date DATE,
    amount NUMERIC(10, 2) NOT NULL,
    description VARCHAR(255),
    status BOOLEAN,
    nm_created VARCHAR(255) NOT NULL,
    dt_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nm_edited VARCHAR(255),
    dt_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);
