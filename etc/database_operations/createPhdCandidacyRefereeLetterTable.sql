
alter table `FILE` add column `KEY_LETTER` int(11);
alter table `FILE` add column `OID_LETTER` bigint(20);
alter table `FILE` add index (`KEY_LETTER`), add index (`OID_LETTER`);

alter table `PUBLIC_CANDIDACY_HASH_CODE` add column `KEY_LETTER` int(11);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add column `OID_LETTER` bigint(20);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add index (`KEY_LETTER`), add index (`OID_LETTER`);

create table `PHD_CANDIDACY_REFEREE_LETTER` (
  `COMMENTS` longtext,
  `COMPARISON_GROUP` longtext,
  `DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CANDIDACY_REFEREE` int(11),
  `KEY_FILE` int(11),
  `KEY_PHD_PROGRAM_CANDIDACY_PROCESS` int(11),
  `KEY_REFEREE_COUNTRY` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `OID` bigint(20),
  `OID_CANDIDACY_REFEREE` bigint(20),
  `OID_FILE` bigint(20),
  `OID_PHD_PROGRAM_CANDIDACY_PROCESS` bigint(20),
  `OID_REFEREE_COUNTRY` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OVERALL_PROMISE` text,
  `RANK` text,
  `RANK_VALUE` longtext,
  `REFEREE_ADDRESS` longtext,
  `REFEREE_CITY` longtext,
  `REFEREE_INSTITUITION` longtext,
  `REFEREE_NAME` longtext,
  `REFEREE_PHONE` longtext,
  `REFEREE_POSITION` longtext,
  `REFEREE_ZIP_CODE` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_CANDIDACY_REFEREE),
  index (OID_CANDIDACY_REFEREE),
  index (KEY_FILE),
  index (OID_FILE),
  index (KEY_PHD_PROGRAM_CANDIDACY_PROCESS),
  index (OID_PHD_PROGRAM_CANDIDACY_PROCESS),
  index (KEY_REFEREE_COUNTRY),
  index (OID_REFEREE_COUNTRY),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

