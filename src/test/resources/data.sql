DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_composition;
DROP TABLE IF EXISTS UserRole;


CREATE TABLE article
(
    id    VARCHAR(100) PRIMARY KEY,
    name  VARCHAR(400) NOT NULL,
    stock VARCHAR(100) NOT NULL
);

CREATE TABLE product
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(400) NOT NULL UNIQUE,
    price VARCHAR(100) DEFAULT '0'
);

CREATE TABLE product_composition
(
    id               SERIAL PRIMARY KEY,
    product_id       INT,
    article_id       VARCHAR(100),
    article_quantity VARCHAR(100)
);

CREATE TABLE UserRole
(
    username VARCHAR(400)  NOT NULL,
    password VARCHAR(400)  NOT NULL,
    roles    VARCHAR(1024) NOT NULL
);
