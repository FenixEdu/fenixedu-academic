


-- Inserted at 2010-04-08T23:39:50.399+01:00

alter table `EMAIL` add column `OID_ROOT_DOMAIN_OBJECT_FROM_EMAIL_QUEUE` bigint(20);
alter table `EMAIL` add index (`OID_ROOT_DOMAIN_OBJECT_FROM_EMAIL_QUEUE`);

create table `MESSAGE_TRANSPORT_RESULT` (
  `CODE` int(11),
  `DESCRIPTION` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_EMAIL` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_EMAIL),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;



-- Inserted at 2010-04-09T16:23:12.837+01:00

alter table `EMAIL` add column `CONFIRMED_ADDRESSES` text;
alter table `EMAIL` add column `FAILED_ADDRESSES` text;
alter table `MESSAGE_ID` add column `SEND_TIME` timestamp NULL default NULL;


