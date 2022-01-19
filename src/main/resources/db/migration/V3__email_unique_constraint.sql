alter table if exists users
    add constraint unique_email unique (email);