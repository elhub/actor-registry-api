--changeset elhub:10
CREATE TABLE template.company_role (
    company_id INT,
    role_id INT,

    constraint fk_company foreign key (company_id) references template.company(id),
    constraint fk_role foreign key (role_id) references template.role(id),
    constraint pk_company_role primary key (company_id, role_id)
);
