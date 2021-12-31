CREATE TABLE IF NOT EXISTS comment
(
    id      BIGINT NOT NULL,
    text    VARCHAR(255),
    post_id BIGINT NOT NULL REFERENCES post,
    user_id BIGINT NOT NULL REFERENCES usr,
    PRIMARY KEY (id, post_id, user_id)
)