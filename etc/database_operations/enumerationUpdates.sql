alter table PERSON MODIFY SEX varchar(6) not null;
update PERSON set SEX = 'MALE' where SEX = 1;
update PERSON set SEX = 'FEMALE' where SEX = 2;
