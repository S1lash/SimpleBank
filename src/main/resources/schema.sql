CREATE TABLE ACCOUNT_OWNER (
  ID NUMBER(22,0) NOT NULL,
  FULL_NAME VARCHAR2(200 byte),
  TYPE VARCHAR2(30 byte) NOT NULL,
  CREATED_DATE TIMESTAMP DEFAULT sysdate NOT NULL,
  ENABLE BOOLEAN DEFAULT TRUE NOT NULL,
  CONSTRAINT OWNER_PK PRIMARY KEY (ID)
);

CREATE TABLE ACCOUNT (
  ID NUMBER(22,0) NOT NULL,
  BALANCE NUMBER(22,0) DEFAULT 0 NOT NULL,
  PAN VARCHAR2(19 byte),
  CREATED_DATE TIMESTAMP DEFAULT sysdate NOT NULL,
  OWNER_ID NUMBER(22,0) NOT NULL,
  NUMBER VARCHAR2(19 byte) NOT NULL,
  ENABLE BOOLEAN DEFAULT TRUE NOT NULL,
  CONSTRAINT ACCOUNT_PK PRIMARY KEY (ID),
  CONSTRAINT ACCOUNT_OWNER_FK FOREIGN KEY (OWNER_ID) REFERENCES ACCOUNT_OWNER (ID)
);
CREATE UNIQUE INDEX ACCOUNT_NUMBER ON ACCOUNT(NUMBER);

CREATE TABLE TERMINAL (
  ID NUMBER(22,0) NOT NULL,
  ADDRESS VARCHAR2(2000 byte),
  TYPE VARCHAR2(30 byte) NOT NULL,
  CREATED_DATE TIMESTAMP DEFAULT sysdate NOT NULL,
  ACCOUNT_ID NUMBER(22,0) NOT NULL,
  ENABLE BOOLEAN DEFAULT TRUE NOT NULL,
  CONSTRAINT TERMINAL_PK PRIMARY KEY (ID),
  CONSTRAINT TERMINAL_ACCOUNT_FK FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ID)
);

CREATE TABLE TRANSACTION (
  ID NUMBER(22,0),
  STATE VARCHAR2(10 byte) NOT NULL,
  AMOUNT NUMBER(22,0) NOT NULL,
  CREATED_DATE TIMESTAMP DEFAULT sysdate NOT NULL,
  DEBIT_ACCOUNT NUMBER(22,0) NOT NULL,
  CREDIT_ACCOUNT NUMBER(22,0) NOT NULL,
  TYPE VARCHAR2(20 byte) NOT NULL,
  CONSTRAINT TRANSACTION_PK PRIMARY KEY (ID),
  CONSTRAINT TRANSACTION_ACCOUNT_FROM_FK FOREIGN KEY (DEBIT_ACCOUNT) REFERENCES ACCOUNT (ID),
  CONSTRAINT TRANSACTION_ACCOUNT_TO_FK FOREIGN KEY (CREDIT_ACCOUNT) REFERENCES ACCOUNT (ID)
);

CREATE SEQUENCE SEQ_ACCOUNT_OWNER START WITH 1;
CREATE SEQUENCE SEQ_ACCOUNT START WITH 1;
CREATE SEQUENCE SEQ_TERMINAL START WITH 1;
CREATE SEQUENCE SEQ_TRANSACTION START WITH 1;