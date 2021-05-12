CREATE TABLE IF NOT EXISTS user_subscriptions
(
    channel_id    INT8 NOT NULL REFERENCES usr,
    subscriber_id INT8 NOT NULL REFERENCES usr,
    PRIMARY KEY (channel_id, subscriber_id)
)