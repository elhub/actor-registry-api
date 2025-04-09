--changeset elhub:2
CREATE TABLE template.company (
    id SERIAL primary key,
    name varchar(50),
    gln varchar(13),
    organization_id varchar(50)
);
