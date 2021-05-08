create table comment
(
    id    bigint not null ,
    text  varchar(255),
    post_id bigint not null references post,
    user_id    bigint not null references usr,
    primary key (id, post_id, user_id)
)