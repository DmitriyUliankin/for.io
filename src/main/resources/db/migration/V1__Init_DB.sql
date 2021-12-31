CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS post
(
    id        INT8          NOT NULL,
    filename  VARCHAR(255),
    name_post VARCHAR(255),
    text      VARCHAR(2048) NOT NULL,
    user_id   INT8,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT8 NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usr
(
    id              INT8         NOT NULL,
    activation_code VARCHAR(255),
    active          BOOLEAN      NOT NULL,
    email           VARCHAR(255),
    password        VARCHAR(255) NOT NULL,
    username        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS message
    ADD CONSTRAINT massage_user_fk FOREIGN KEY (user_id) REFERENCES usr;

ALTER TABLE IF EXISTS user_role
    ADD CONSTRAINT user_role_user_fk FOREIGN KEY (user_id) REFERENCES usr;