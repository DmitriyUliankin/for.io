create table post_likes
(
    user_id    bigint not null references usr,
    post_id bigint not null references post,
    primary key (user_id, post_id)
)