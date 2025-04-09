--changeset elhub:3
CREATE TABLE template.comment (
    id SERIAL PRIMARY KEY,
    userId INT,
    body TEXT
);
