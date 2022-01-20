create table coach_information
(
    id uuid not null,
    availability varchar(255) not null,
    coach_xp     integer not null,
    introduction varchar(255) not null,
    primary key (id)
);
create table coach_information_topics
(
    coach_information_id uuid not null,
    topics_id uuid not null,
    primary key (coach_information_id, topics_id)
);
create table topics
(
    id uuid not null,
    topic_name varchar(255) not null,
    primary key (id)
);
create table users
(
    id uuid not null,
    company   varchar(255) not null,
    email     varchar(255) not null,
    firstname varchar(255) not null,
    lastname  varchar(255) not null,
    password  varchar(255) not null,
    roles     varchar(255) not null,
    coach_id uuid not null,
    primary key (id)
);
alter table coach_information_topics
    add constraint FK_topics_id foreign key (topics_id) references topics;
alter table coach_information_topics
    add constraint FK_coach_information_id foreign key (coach_information_id) references coach_information;
alter table users
    add constraint FK_coach_id foreign key (coach_id) references coach_information;