


-- Inserted at 2009-03-27T12:38:54.154Z

alter table VIGILANCY change column KEY_VIGILANT KEY_VIGILANT int(11);
alter table UNAVAILABLE_PERIOD change column KEY_VIGILANT KEY_VIGILANT int(11);

alter table `PARTY` add column `KEY_INCOMPATIBLE_VIGILANT` int(11);
alter table `PARTY` add column `KEY_PERSON` int(11);
alter table `PARTY` add index (`KEY_INCOMPATIBLE_VIGILANT`);
alter table `PARTY` add index (`KEY_PERSON`);
alter table `UNAVAILABLE_PERIOD` add column `KEY_PERSON` int(11);
alter table `UNAVAILABLE_PERIOD` add index (`KEY_PERSON`);
alter table `VIGILANCY` add column `KEY_VIGILANT_WRAPPER` int(11);
alter table `VIGILANCY` add index (`KEY_VIGILANT_WRAPPER`);


create table `VIGILANT_WRAPPER` (
  `CONVOKABLE` tinyint(1),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `JUSTIFICATION` text,
  `KEY_PERSON` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_VIGILANT_GROUP` int(11),
  `POINTS_WEIGHT` text,
  `START_POINTS` text,
  primary key (ID_INTERNAL),
  index (KEY_PERSON),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_VIGILANT_GROUP)
) type=InnoDB, character set latin1 ;

