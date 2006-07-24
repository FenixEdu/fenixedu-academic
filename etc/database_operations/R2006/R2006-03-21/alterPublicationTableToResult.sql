alter table PUBLICATION_DATA rename RESULT;

alter table RESULT change column TITLE TITLE_OLD varchar(250);
alter table RESULT add column TITLE longtext;

alter table RESULT change column OBSERVATION DESCRIPTION_OLD varchar(250);
alter table RESULT add column DESCRIPTION longtext;

alter table RESULT change column PUBLICATION_TYPE TYPE varchar(250);
alter table AUTHORSHIP change column KEY_PUBLICATION KEY_RESULT int(11) NOT NULL;


alter table RESULT add column PRODUCT_INTERNAL_ID varchar(100);
alter table RESULT add column PATENT_NUMBER varchar(200);
alter table RESULT add column STATUS varchar(250);
alter table RESULT add column KEY_COUNTRY int(11);
alter table RESULT add column REGISTRATION_DATE	varchar(10);
alter table RESULT add column APPROVAL_DATE varchar(10);
alter table RESULT add column CLASS_NAME varchar(255) NOT NULL;

UPDATE RESULT SET CLASS_NAME = "net.sourceforge.fenixedu.domain.research.result.Publication";

update RESULT set TITLE = (select concat("pt",length(TITLE_OLD),":",TITLE_OLD));
update RESULT set DESCRIPTION = (select concat("pt",length(DESCRIPTION_OLD),":",DESCRIPTION_OLD));


