-- Inserted at 2009-04-03T18:49:18.214+01:00
alter table `ACCOUNTABILITY` add column `KEY_SENDER` int(11);
alter table `ACCOUNTABILITY` add index (`KEY_SENDER`);
alter table `SENDER` add column `KEY_PERSON_FUNCTION` int(11);
alter table `SENDER` add index (`KEY_PERSON_FUNCTION`);
