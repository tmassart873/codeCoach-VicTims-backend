create table session
(
    id UUID not null primary key,
    coach_id UUID not null,
    fk_coachee_id UUID not null,
    subject varchar(255),
    date date,
    time time,
    location varchar(255),
    remarks varchar(255),
    isValid boolean,

    foreign key (fk_coachee_id) references app_user
);