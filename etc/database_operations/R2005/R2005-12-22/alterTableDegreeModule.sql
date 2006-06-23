alter table DEGREE_MODULE change column NAME NAME varchar(100) default NULL;
alter table DEGREE_MODULE change column CODE CODE varchar(50) default NULL;
alter table DEGREE_MODULE change column ENROLLMENT_ALLOWED ENROLLMENT_ALLOWED tinyint(1) default '1';
alter table DEGREE_MODULE change column ACRONYM ACRONYM varchar(20) default NULL;
alter table DEGREE_MODULE drop key U1;
alter table DEGREE_MODULE drop key KEY_DEGREE_CURRICULAR_PLAN;

alter table DEGREE_MODULE add column PREREQUISITES text;
alter table DEGREE_MODULE add column PREREQUISITES_EN text;
