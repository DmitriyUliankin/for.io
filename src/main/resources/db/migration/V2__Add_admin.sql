INSERT INTO usr (id, username, password, active)
VALUES (0, 'admin', '123', true);

INSERT INTO user_role (user_id, roles)
VALUES (0, 'USER'),
       (0, 'ADMIN');