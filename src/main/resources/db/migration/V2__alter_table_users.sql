alter table users
alter column coach_id
drop not null ;

alter table coach_information
alter column availability
drop not null;

alter table coach_information
alter column coach_xp
drop not null;

alter table coach_information
alter column introduction
drop not null;

