alter table `JOB` CHANGE column BEGIN_DATE BEGIN_DATE DATE DEFAULT NULL;
alter table `JOB` add column `OID_PARENT_BUSINESS_AREA` bigint(20);
alter table `JOB` add index (`OID_PARENT_BUSINESS_AREA`);
alter table `JOB` add column `LAST_MODIFIED_DATE` timestamp NULL default NULL;
alter table `JOB` add column `SALARY` double;
alter table `QUALIFICATION` add column `INSTITUTION_TYPE` text;
alter table `QUALIFICATION` add column `OID_BASE_INSTITUTION` bigint(20);
alter table `QUALIFICATION` add index (`OID_BASE_INSTITUTION`);
alter table `QUALIFICATION` add column `OID_COUNTRY_UNIT` bigint(20);
alter table `QUALIFICATION` add index (`OID_COUNTRY_UNIT`);

alter table `PARTY_CONTACT` add column `LAST_MODIFIED_DATE` timestamp NULL default NULL;
