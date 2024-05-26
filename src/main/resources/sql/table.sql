CREATE TABLE customers
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(256),
    last_name  VARCHAR(256),
    email      VARCHAR(256) UNIQUE NOT NULL
);

TRUNCATE TABLE customers RESTART IDENTITY;

INSERT INTO customers (first_name, last_name, email)
VALUES ('Steven', 'Lopez', 'steven.lopez@bestmail.com'),
       ('Eileen', 'Brown', 'eileen.brown@bestmail.com'),
       ('Marlene', 'Cooper', 'marlene.cooper@bestmail.com'),
       ('Sue', 'Soto', 'sue.soto@bestmail.com'),
       ('Tony', 'Davis', 'tony.davis@bestmail.com'),
       ('Maria', 'Thomas', 'maria.thomas@bestmail.com'),
       ('Betty', 'Lucas', 'betty.lucas@bestmail.com'),
       ('Samuel', 'Morgan', 'samuel.morgan@bestmail.com'),
       ('Judith', 'Carr', 'judith.carr@bestmail.com'),
       ('Larry', 'Wheeler', 'larry.wheeler@bestmail.com');

SELECT *
FROM customers;