drop table EXAMPLE;
drop sequence SEQ_EXAMPLE_PK;

create table EXAMPLE (
    id identity not null,
    data varchar(255) not null,
    EXAMPLE_NAME varchar(200) not null,
    primary key (id)
);

create sequence SEQ_EXAMPLE_PK start with 1 increment by 1;