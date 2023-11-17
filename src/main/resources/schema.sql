DROP TABLE IF EXISTS invoice_items;
DROP TABLE IF EXISTS invoices;

CREATE TABLE invoices (
                           id SERIAL PRIMARY KEY,
                           invoice_reference TEXT,
                           created_at DATE,
                           payment_due DATE,
                           description TEXT,
                           payment_terms INT,
                           client_name TEXT,
                           client_email TEXT,
                           invoice_status TEXT,
                           sender_street TEXT,
                           sender_city TEXT,
                           sender_post_code TEXT,
                           sender_country TEXT,
                           client_street TEXT,
                           client_city TEXT,
                           client_post_code TEXT,
                           client_country TEXT,
                           total NUMERIC(10, 2)
);

CREATE TABLE invoice_items (
                               id SERIAL PRIMARY KEY,
                               invoice_id INTEGER REFERENCES invoices(id),
                               name TEXT,
                               quantity INTEGER,
                               price NUMERIC(10, 2),
                               total NUMERIC(10, 2)
);