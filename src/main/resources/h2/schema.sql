DROP TABLE user IF EXISTS;
    create table user (
       battletag varchar(255) not null,
        role varchar(255) not null,
        primary key (battletag)
    );