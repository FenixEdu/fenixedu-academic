
create table `PHD_PROGRAM` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `CREATOR` varchar(255) not null,
  `KEY_DEGREE` int(11) null,
  `KEY_PHD_PROGRAM_UNIT` int(11) null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `WHEN_CREATED` datetime not NULL,
  primary key (ID_INTERNAL),
  index (KEY_DEGREE),
  index (KEY_PHD_PROGRAM_UNIT),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;


-- Inserted at 2009-04-28T11:43:15.233+01:00
alter table `CANDIDACY` add column `KEY_CANDIDACY_PROCESS` int(11);
alter table `CANDIDACY` add index (`KEY_CANDIDACY_PROCESS`);
alter table `EVENT` add column `KEY_CANDIDACY_PROCESS` int(11);
alter table `EVENT` add index (`KEY_CANDIDACY_PROCESS`);
alter table `PARTY` add column `KEY_PHD_PROGRAM` int(11);
alter table `PARTY` add index (`KEY_PHD_PROGRAM`);
alter table `PROCESS` add column `KEY_EVENT` int(11);
alter table `PROCESS` add column `KEY_INDIVIDUAL_PROGRAM_PROCESS` int(11);
alter table `PROCESS` add column `KEY_PERSON` int(11);
alter table `PROCESS` add column `KEY_PHD_PROGRAM` int(11);
alter table `PROCESS` add column `KEY_REGISTRATION` int(11);
alter table `PROCESS` add index (`KEY_EVENT`);
alter table `PROCESS` add index (`KEY_INDIVIDUAL_PROGRAM_PROCESS`);
alter table `PROCESS` add index (`KEY_PERSON`);
alter table `PROCESS` add index (`KEY_PHD_PROGRAM`);
alter table `PROCESS` add index (`KEY_REGISTRATION`);
alter table `REGISTRATION` add column `KEY_PHD_INDIVIDUAL_PROGRAM_PROCESS` int(11);
alter table `REGISTRATION` add index (`KEY_PHD_INDIVIDUAL_PROGRAM_PROCESS`);



