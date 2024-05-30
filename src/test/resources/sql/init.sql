insert into app_users (username, email, password)
values ('User', '1@1.com', '$2a$10$AH9Ibo3No9xCUHb2NMfBLe9O9tISboeE/FGiceZ8zXkt.OGHpIZMG');

SELECT setval ('app_users_id_seq', (SELECT MAX(id) FROM app_users));
