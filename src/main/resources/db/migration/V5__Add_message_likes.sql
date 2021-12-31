CREATE TABLE IF NOT EXISTS post_likes
(
    user_id BIGINT NOT NULL REFERENCES usr,
    post_id BIGINT NOT NULL REFERENCES post,
    PRIMARY KEY (user_id, post_id)
)