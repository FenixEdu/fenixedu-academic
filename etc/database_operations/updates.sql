-- Inserted at 2009-08-11T11:22:36.476+01:00

alter table `EVENT` add column `KEY_PROCESS` int(11);
alter table `EVENT` add column `OID_PROCESS` bigint(20);
alter table `EVENT` add index (`KEY_PROCESS`), add index (`OID_PROCESS`);

alter table `PROCESS` add column `KEY_REGISTRATION_FEE` int(11);
alter table `PROCESS` add column `OID_REGISTRATION_FEE` bigint(20);
alter table `PROCESS` add index (`KEY_REGISTRATION_FEE`), add index (`OID_REGISTRATION_FEE`);

alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
