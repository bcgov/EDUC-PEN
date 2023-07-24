CREATE TABLE TENANT_ACCESS
(
    TENANT_ACCESS_ID         RAW(16)              NOT NULL,
    CLIENT_ID                VARCHAR2(255)        NOT NULL,
    TENANT_ID                VARCHAR2(255)        NOT NULL,
    CREATE_USER              VARCHAR2(32)         NOT NULL,
    CREATE_DATE              DATE DEFAULT SYSDATE NOT NULL,
    UPDATE_USER              VARCHAR2(32)         NOT NULL,
    UPDATE_DATE              DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT DIGITAL_IDENTITY_PK PRIMARY KEY (TENANT_ACCESS_ID)
);
