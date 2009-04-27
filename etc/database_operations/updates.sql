alter table `ANNOUNCEMENT_CATEGORY` add index(`KEY_ROOT_DOMAIN_OBJECT`);
alter table `CONTENT` add index (`KEY_CAMPUS`);
alter table `PARTY` add column `KEY_SENDER` int(11);
alter table `PARTY` add index (`KEY_SENDER`);
alter table `SENDER` add column `KEY_PERSON` int(11);
alter table `SENDER` add index (`KEY_PERSON`);