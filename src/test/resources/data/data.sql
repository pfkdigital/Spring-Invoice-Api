INSERT INTO invoices (invoice_reference, created_at, payment_due, description, payment_terms, client_name, client_email, invoice_status, sender_street, sender_city, sender_post_code, sender_country, client_street, client_city, client_post_code, client_country, total)
VALUES
    ('RT3080', '2021-08-18', '2021-08-19', 'Re-branding', 1, 'Jensen Huang', 'jensenh@mail.com', 'paid', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '106 Kendell Street', 'Sharrington', 'NR24 5WQ', 'United Kingdom', 1800.90),
    ('XM9141', '2021-08-21', '2021-09-20', 'Graphic Design', 30, 'Alex Grim', 'alexgrim@mail.com', 'pending', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '84 Church Way', 'Bradford', 'BD1 9PB', 'United Kingdom', 556.00),
    ('RG0314', '2021-09-24', '2021-10-01', 'Website Redesign', 7, 'John Morrison', 'jm@myco.com', 'paid', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '79 Dover Road', 'Westhall', 'IP19 3PF', 'United Kingdom', 14002.33),
    ('RT2080', '2021-10-11', '2021-10-12', 'Logo Concept', 1, 'Alysa Werner', 'alysa@email.co.uk', 'pending', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '63 Warwick Road', 'Carlisle', 'CA20 2TG', 'United Kingdom', 102.04),
    ('AA1449', '2021-10-7', '2021-10-14', 'Re-branding', 7, 'Mellisa Clarke', 'mellisa.clarke@example.com', 'pending', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '46 Abbey Row', 'Cambridge', 'CB5 6EG', 'United Kingdom', 4032.33),
    ('TY9141', '2021-10-01', '2021-10-31', 'Landing Page Design', 30, 'Thomas Wayne', 'thomas@dc.com', 'pending', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '3964 Queens Lane', 'Gotham', '60457', 'United States of America', 6155.91),
    ('FV2353', '2021-11-05', '2021-11-12', 'Logo Re-design', 7, 'Anita Wainwright', '', 'draft', '19 Union Terrace', 'London', 'E1 3EZ', 'United Kingdom', '', '', '', '', 3102.04);

-- Insert data into the `invoice_items` table
INSERT INTO invoice_items (invoice_id, name, quantity, price, total)
VALUES
    (1, 'Brand Guidelines', 1, 1800.90, 1800.90),
    (2, 'Banner Design', 1, 156.00, 156.00),
    (2, 'Email Design', 2, 200.00, 400.00),
    (3, 'Website Redesign', 1, 14002.33, 14002.33),
    (4, 'Logo Sketches', 1, 102.04, 102.04),
    (5, 'New Logo', 1, 1532.33, 1532.33),
    (5, 'Brand Guidelines', 1, 2500.00, 2500.00),
    (6, 'Web Design', 1, 6155.91, 6155.91),
    (7, 'Logo Re-design', 1, 3102.04, 3102.04);
