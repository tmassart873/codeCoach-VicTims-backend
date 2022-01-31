alter table session
rename column fk_coachee_id to coachee_id;

alter table session
add constraint fk_coach_id_session foreign key (coach_id) REFERENCES app_user (id);