INSERT INTO ACCOUNT_OWNER (ID, FULL_NAME, TYPE, ENABLE)
VALUES
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-1', 'BANK', TRUE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-2', 'INDIVIDUAL', TRUE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-3', 'INDIVIDUAL', FALSE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-4', 'INDIVIDUAL', TRUE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-5', 'ORGANIZATION', TRUE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-6', 'INDIVIDUAL', TRUE),
(SEQ_ACCOUNT_OWNER.nextval, 'OWNER NAME-7', 'INDIVIDUAL', TRUE);

INSERT INTO ACCOUNT (ID, BALANCE, PAN, OWNER_ID, NUMBER, ENABLE)
VALUES
(SEQ_ACCOUNT.nextval, 99999999999999, NULL, 1, '0000000000000000000', TRUE),
(SEQ_ACCOUNT.nextval, 910000, NULL, 2, '0000000000000000002', FALSE),
(SEQ_ACCOUNT.nextval, 79988888, '1111111111111113', 3, '0000000000000000003', TRUE),
(SEQ_ACCOUNT.nextval, 8000000000, '1111111111111114', 4, '0000000000000000004', TRUE),
(SEQ_ACCOUNT.nextval, 800000000000, NULL, 5, '0000000000000000005', TRUE),
(SEQ_ACCOUNT.nextval, 970000, '1111111111111116', 6, '0000000000000000006', TRUE),
(SEQ_ACCOUNT.nextval, 324555096, NULL, 4, '0000000000000000007', TRUE),
(SEQ_ACCOUNT.nextval, 0, NULL, 4, '0000000000000000008', TRUE),
(SEQ_ACCOUNT.nextval, 9223372036854775806, NULL, 6, '0000000000000000009', TRUE);

INSERT INTO TERMINAL (ID, ADDRESS, TYPE, ACCOUNT_ID, ENABLE)
VALUES
(SEQ_TERMINAL.nextval, 'Moscow, Red Square, 1', 'ATM', 1, TRUE),
(SEQ_TERMINAL.nextval, 'Moscow, Red Square, 1', 'POS', 1, TRUE);

INSERT INTO TRANSACTION (ID, STATE, AMOUNT, DEBIT_ACCOUNT, CREDIT_ACCOUNT, TYPE)
VALUES
(SEQ_TRANSACTION.nextval, 'APPROVED', 234000, 4, 1, 'DEPOSIT'),
(SEQ_TRANSACTION.nextval, 'APPROVED', 349003, 4, 5, 'TRANSFER'),
(SEQ_TRANSACTION.nextval, 'DECLINE', 23400, 6, 2, 'TRANSFER'),
(SEQ_TRANSACTION.nextval, 'APPROVED', 800000, 1, 6, 'WITHDRAWAL');
