-- changeset elhub:4
INSERT INTO template.user (id, name) VALUES (1, 'Alice');
INSERT INTO template.user (id, name) VALUES (2, 'Bob');
-- Set the sequence to the next value
SELECT setval('template.user_id_seq', 3);
