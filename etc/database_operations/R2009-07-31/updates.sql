-- Inserted at 2009-07-28T15:06:27.248+01:00
alter table `PROCESS` add column `WHEN_FORMALIZED_REGISTRATION` varchar(10) null;
alter table `PROCESS` add column `WHEN_STARTED_STUDIES` varchar(10) null;

alter table PHD_ALERT_MESSAGE drop column TASK_PERFORMED;
alter table PHD_ALERT_MESSAGE drop column KEY_PERSON;
alter table PHD_ALERT_MESSAGE drop column OID_PERSON;
alter table `PHD_ALERT_MESSAGE` add column `READED` tinyint(1);
alter table `PHD_ALERT_MESSAGE` add column `KEY_PERSON_WHO_MARKED_AS_READED` int(11) null;
alter table `PHD_ALERT_MESSAGE` add column `OID_PERSON_WHO_MARKED_AS_READED` bigint(20) null;
alter table `PHD_ALERT_MESSAGE` add index (`KEY_PERSON_WHO_MARKED_AS_READED`), add index (`OID_PERSON_WHO_MARKED_AS_READED`);

create table `PHD_ALERT_MESSAGE_PERSON` (KEY_PERSON int(11) not null, KEY_PHD_ALERT_MESSAGE int(11) not null, OID_PERSON bigint unsigned not null, OID_PHD_ALERT_MESSAGE bigint unsigned not null,  primary key (KEY_PERSON, KEY_PHD_ALERT_MESSAGE), key(KEY_PERSON), key(KEY_PHD_ALERT_MESSAGE), key (OID_PERSON, OID_PHD_ALERT_MESSAGE), key(OID_PERSON), key(OID_PHD_ALERT_MESSAGE)) type=InnoDB;


