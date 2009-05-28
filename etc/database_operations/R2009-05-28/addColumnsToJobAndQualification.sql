
alter table `JOB` add column `KEY_CREATOR` int(11);
alter table `JOB` add column `OID_CREATOR` bigint(20);
alter table `JOB` add index (`KEY_CREATOR`), add index (`OID_CREATOR`);

alter table `QUALIFICATION` add column `KEY_CREATOR` int(11);
alter table `QUALIFICATION` add column `OID_CREATOR` bigint(20);
alter table `QUALIFICATION` add index (`KEY_CREATOR`), add index (`OID_CREATOR`);
