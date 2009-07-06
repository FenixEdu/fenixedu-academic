
alter table `PHD_ALERT` add column `FIRE_DATE` datetime NULL default NULL;
alter table `PHD_ALERT` add column `KEY_REFEREE` int(11);
alter table `PHD_ALERT` add column `OID_REFEREE` bigint(20);
alter table `PHD_ALERT` add column `WHEN_TO_FIRE` text;
alter table `PHD_ALERT` add index (`KEY_REFEREE`), add index (`OID_REFEREE`);
