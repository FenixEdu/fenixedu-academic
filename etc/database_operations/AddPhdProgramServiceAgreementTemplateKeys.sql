alter table `PHD_PROGRAM` add column `KEY_SERVICE_AGREEMENT_TEMPLATE` int(11) not null, add column `OID_SERVICE_AGREEMENT_TEMPLATE` bigint unsigned default not null;
alter table `PHD_PROGRAM` add index (`KEY_SERVICE_AGREEMENT_TEMPLATE`), add index (`OID_SERVICE_AGREEMENT_TEMPLATE`);
alter table `SERVICE_AGREEMENT_TEMPLATE` add column `KEY_PHD_PROGRAM` int(11) null, add column `OID_PHD_PROGRAM` bigint unsigned default null;
alter table `SERVICE_AGREEMENT_TEMPLATE` add index (`KEY_PHD_PROGRAM`), add index (`OID_PHD_PROGRAM`);

