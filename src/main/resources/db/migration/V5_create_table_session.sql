create table session
(
    id UUID not null primary key,
    coach_id UUID not null,
    coachee_id UUID not null,
    subject varchar(255),
    date date,
    time time,
    location varchar(255),
    remarks varchar(255),
    isValid boolean,

    foreign key (coach_id) references app_user(coach_id),
    foreign key (coachee_id) references app_user(id)
);