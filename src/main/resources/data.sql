CREATE TABLE IF NOT EXISTS item (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    price INT
);

CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT PRIMARY KEY,
    item_id BIGINT,
    qty INT,
    type VARCHAR(1),
    CONSTRAINT fk_inventory_item FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE IF NOT EXISTS "order" (
    order_no VARCHAR(10) PRIMARY KEY,
    item_id BIGINT,
    qty INT,
    price INT,
    CONSTRAINT fk_order_item FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE IF NOT EXISTS sequence (
  uuid VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  ordinal BIGINT DEFAULT NULL,
  last_update DATE DEFAULT NULL,
  PRIMARY KEY (uuid),
  UNIQUE (name)
);


INSERT INTO sequence
(uuid, name, ordinal, last_update)VALUES
('7a01c1e8-dec8-4c6b-8f89-89c753c061c1', 'INVENTORY', 9, '2025-04-24'),
('7a01c1e8-dec8-4c6b-8f89-89c753c061c2', 'ITEM', 7, '2025-04-24'),
('7a01c1e8-dec8-4c6b-8f89-89c753c061c3', 'O', 11, '2025-04-24');


INSERT INTO item (id, name, price) VALUES
(1, 'Pen', 5),
(2, 'Book', 10),
(3, 'Bag', 30),
(4, 'Pencil', 3),
(5, 'Shoe', 45),
(6, 'Box', 5),
(7, 'Cap', 25);

INSERT INTO "order" (order_no, item_id, qty, price) VALUES
('O1', 1, 2, 5),
('O2', 2, 3, 10),
('O3', 5, 4, 45),
('O4', 4, 1, 2),
('O5', 5, 2, 45),
('O6', 6, 3, 5),
('O7', 1, 5, 5),
('O8', 2, 4, 10),
('O9', 3, 2, 30),
('O10', 4, 3, 3);

INSERT INTO inventory (id, item_id, qty, type) VALUES
(1, 1, 5, 'T'),
(2, 2, 10, 'T'),
(3, 3, 30, 'T'),
(4, 4, 3, 'T'),
(5, 5, 45, 'T'),
(6, 6, 5, 'T'),
(7, 7, 25, 'T'),
(8, 4, 7, 'T'),
(9, 5, 10, 'W');