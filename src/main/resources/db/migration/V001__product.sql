CREATE TABLE product (
    id varchar(50) NOT NULL,
    name varchar(50) NULL,
    description varchar(2000) NULL,
    currency varchar(3) NULL,
    price numeric(10, 2) NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);
