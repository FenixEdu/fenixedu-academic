
alter table `FILE` add column `OID_ELEMENT` bigint(20), add index (`OID_ELEMENT`);
alter table `PROCESS` add column `OID_FEEDBACK_REQUEST` bigint(20), add column `SHARED_DOCUMENTS` text;

create table `PHD_CANDIDACY_FEEDBACK_REQUEST_ELEMENT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20) NOT NULL,
  `OID_PARTICIPANT` bigint(20) NOT NULL,
  `OID_PROCESS` bigint(20) NOT NULL,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) NOT NULL,
  `MAIL_SUBJECT` longtext NOT NULL,
  `MAIL_BODY` longtext NOT NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PARTICIPANT),
  index (OID_PROCESS),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
