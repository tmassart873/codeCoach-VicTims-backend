alter table users
drop column password;

alter table users
rename to app_user;