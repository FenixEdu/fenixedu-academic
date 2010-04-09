
-- Inserted at 2010-04-09T16:23:12.837+01:00

alter table `EMAIL` add column `CONFIRMED_ADDRESSES` text;
alter table `EMAIL` add column `FAILED_ADDRESSES` text;
alter table `MESSAGE_ID` add column `SEND_TIME` timestamp NULL default NULL;


