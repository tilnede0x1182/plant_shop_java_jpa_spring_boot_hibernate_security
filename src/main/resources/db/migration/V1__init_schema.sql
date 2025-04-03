CREATE TABLE IF NOT EXISTS plant (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL NOT NULL,
    category TEXT,
    stock INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS app_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    address TEXT,
    phone TEXT,
    active BOOLEAN NOT NULL DEFAULT true,
    registration_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
