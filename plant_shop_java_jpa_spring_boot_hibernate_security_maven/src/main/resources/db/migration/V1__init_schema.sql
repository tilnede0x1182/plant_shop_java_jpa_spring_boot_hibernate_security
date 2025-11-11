CREATE TABLE plant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(255) NOT NULL
);

CREATE TABLE customer_order (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    status VARCHAR(255),
    user_id INT
);

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    plant_id INT NOT NULL,
    customer_order_id INT NOT NULL,
    FOREIGN KEY (plant_id) REFERENCES plant(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_order_id) REFERENCES customer_order(id)
);
