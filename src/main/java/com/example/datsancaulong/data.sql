use project;

insert into roles(role)
values ('USER'),
       ('MANAGER'),
       ('ADMIN');

# Thêm tài khoản
insert into users(email, enabled, full_name, password, phone)
values ('tienloc@gmail.com', true, 'Nguyen tien loc', '$2a$10$lW6WyrLy.e2J9d.yBdDv8.KwHBreI1JipVeC3RJbrXpvXgkVgDqKO',
        '0329950482'),
       ('ngueynloc@gmail.com', true, 'Nguyen tien loc', '$2a$10$lW6WyrLy.e2J9d.yBdDv8.KwHBreI1JipVeC3RJbrXpvXgkVgDqKO',
        '0329950482');


# Gắn role
insert user_roles(user_id, role_id)
VALUES (1, 3),
       (2, 1);