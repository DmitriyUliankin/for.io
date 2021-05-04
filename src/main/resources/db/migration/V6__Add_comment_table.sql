create table comment
(
    id    bigint not null ,
    text  varchar(255),
    message_id bigint not null references message,
    user_id    bigint not null references usr,
    primary key (id, message_id, user_id)
)