insert into authors (full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres (name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books (title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres (book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments (text, book_id)
values ('text_1', 1), ('text_2', 2), ('text_3', 1);

insert into roles (name)
values ('ADMIN'), ('USER');

insert into users (username, password)
values ('admin', '$2a$10$TiS3QX0I294eY0CYDtvL2.QCNSz6N2I.LEyXvhQ03yyqXt4uoAxe6'),
       ('user', '$2a$10$/5JpkkvA4wqXzC3ajC/4t.X9ftBPw67/O7CyNZw1BlNMm4xLNlloK');

insert into users_roles (user_id, role_id)
values (1, 1), (2, 2);
