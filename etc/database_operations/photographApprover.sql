alter table `PHOTOGRAPH` add column `OID_APPROVER` bigint(20);
alter table `PHOTOGRAPH` add index (`OID_APPROVER`);
