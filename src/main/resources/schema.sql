create table if not exists employee (
    id bigserial not null,
    name varchar not null,
    position varchar not null,
    salary integer,
    primary key (id)
);