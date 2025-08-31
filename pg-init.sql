CREATE SCHEMA IF NOT EXISTS "public";

CREATE TYPE customer_role AS ENUM
(
    'USER', 'STAFF', 'ADMIN'
);

CREATE TABLE customer
(
    "id" bigint PRIMARY KEY
        GENERATED ALWAYS AS IDENTITY (START 1048576 INCREMENT BY 1),

    "role" customer_role NOT NULL,

    "username" varchar(30) NOT NULL UNIQUE,
    "password" varchar(61) NOT NULL,

    "thirdparty_token" varchar(63) NOT NULL UNIQUE
);

CREATE TABLE receipt
(
    "fn" varchar(31) NOT NULL,
    "fd" varchar(31) NOT NULL,
    "fp" varchar(31) NOT NULL,

    "total"
        integer
        NOT NULL,

    "timestamp"
        timestamp without time zone
        NOT NULL,

    "foundation"
        varchar
        NOT NULL,

    "customer_id"
        bigint
        REFERENCES customer("id"),

    PRIMARY KEY ("fn", "fd", "fp")
);
