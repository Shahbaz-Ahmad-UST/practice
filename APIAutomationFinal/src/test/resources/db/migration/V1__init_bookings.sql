CREATE TABLE IF NOT EXISTS bookings (
    id              VARCHAR(64)     NOT NULL PRIMARY KEY,
    pnr             VARCHAR(32)     NOT NULL UNIQUE,
    emp_id          VARCHAR(32)     NOT NULL,
    journey_type    VARCHAR(32)     NOT NULL,
    inventory_id    VARCHAR(64)     NOT NULL,
    state           VARCHAR(32)     NOT NULL,
    seat_ids        JSON            NOT NULL,
    amount_paise    BIGINT          NOT NULL,
    refundable      BOOLEAN         NOT NULL DEFAULT TRUE,
    hold_expires_at TIMESTAMP       NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_pnr (pnr),
    INDEX idx_emp_id (emp_id)
);