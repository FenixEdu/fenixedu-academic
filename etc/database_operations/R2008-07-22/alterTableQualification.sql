alter table QUALIFICATION add column OJB_CONCRETE_CLASS varchar(255);
update QUALIFICATION set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.Qualification';
alter table QUALIFICATION add column FORMATION_TYPE varchar(100) default null;
alter table QUALIFICATION add column BEGIN_YEAR varchar(10) default null;
alter table QUALIFICATION add column ECTS_CREDITS  varchar(255) default null;
alter table QUALIFICATION add column FORMATION_HOURS int(11) default null;
alter table QUALIFICATION add column KEY_EDUCATION_AREA int(11) unsigned default null;
alter table QUALIFICATION add column KEY_INSTITUTION int(11) unsigned default null;
