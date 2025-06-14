insert into categories (id, name) values (1, 'Fantasy');
insert into books (id, title, author, isbn, price)
values (1, 'Witcher', 'Sapkovsky', '1981112314470', 30.75);
insert into books_categories (book_id, category_id) values (1, 1);

insert into categories (id, name) values (2, 'Adventure');
insert into books (id, title, author, isbn, price)
values (2, 'Monte Crysto', 'Duma', '1981112315470', 30.50);
insert into books_categories (book_id, category_id) values (2, 2);

insert into books (id, title, author, isbn, price)
values (3, 'TestTitle', 'TestAuthor', '1981112314471', 30.75);
insert into books_categories (book_id, category_id) values (3, 1);

insert into users (id, email, password, first_name, last_name, is_deleted)
values (1, 'test@gmail.com', 'test1pass', 'First', 'Last', false);
insert into shopping_carts (id, is_deleted) values (1, false);
insert into cart_items (id, shopping_cart_id, book_id, quantity)
values (1, 1, 1, 1);
