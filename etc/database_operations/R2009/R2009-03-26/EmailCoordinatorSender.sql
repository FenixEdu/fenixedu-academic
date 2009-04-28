alter table `DEGREE` add column `KEY_SENDER` int(11);
alter table `DEGREE` add index (`KEY_SENDER`);
alter table `SENDER` add column `KEY_DEGREE` int(11);
alter table `SENDER` add index (`KEY_DEGREE`);


