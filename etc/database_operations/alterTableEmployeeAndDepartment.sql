alter table ass_FUNCIONARIO add column KEY_SALARY_UNIT int(11) unsigned default null;
alter table ass_FUNCIONARIO add column KEY_MAILING_UNIT int(11) unsigned default null;
alter table ass_FUNCIONARIO add column KEY_WORKING_UNIT int(11) unsigned default null;

alter table DEPARTMENT add column KEY_UNIT int(11) unsigned default null;