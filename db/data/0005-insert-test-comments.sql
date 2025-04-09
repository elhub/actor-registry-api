-- changeset elhub:4
INSERT INTO template.comment (id, userId, body)
VALUES (1, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO template.comment (id, userId, body)
VALUES (2, 2, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');
INSERT INTO template.comment (id, userId, body)
VALUES (3, 1, 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.');
INSERT INTO template.comment (id, userId, body)
VALUES (4, 2, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');
INSERT INTO template.comment (id, userId, body)
VALUES (5, 2, 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.');
INSERT INTO template.comment (id, userId, body)
VALUES (6, 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit.');
-- Set the sequence to the next value
SELECT setval('template.comment_id_seq', 7);
