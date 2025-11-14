CREATE TABLE sms (
    id CHAR(36) PRIMARY KEY,
    sid VARCHAR(255),
    name VARCHAR(255),
    account VARCHAR(255),
    amount FLOAT,
    tax FLOAT,
    is_received BOOLEAN,
    date TIMESTAMP,
    operation VARCHAR(50) 
);
