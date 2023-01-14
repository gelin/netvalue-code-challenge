create table if not exists user (
    id varchar primary key,
    name varchar,
    password varchar
)
unique (name)
;

