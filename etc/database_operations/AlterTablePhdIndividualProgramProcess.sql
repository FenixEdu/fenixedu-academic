alter table `PROCESS` add column `COLLABORATION_TYPE` text;
alter table `PROCESS` add column `OTHER_COLLABORATION_TYPE` text;
alter table `PROCESS` add column `KEY_EXECUTION_YEAR` int(11) null, add column `OID_EXECUTION_YEAR` bigint unsigned default null;
alter table `PROCESS` add index (`KEY_EXECUTION_YEAR`), add index (`OID_EXECUTION_YEAR`);



