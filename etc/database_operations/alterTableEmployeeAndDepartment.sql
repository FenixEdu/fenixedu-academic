alter table ass_FUNCIONARIO drop column KEY_SALARY_UNIT;
alter table ass_FUNCIONARIO drop column KEY_MAILING_UNIT;
alter table ass_FUNCIONARIO drop column KEY_WORKING_UNIT;

alter table DEPARTMENT add column KEY_UNIT int(11) unsigned default null;