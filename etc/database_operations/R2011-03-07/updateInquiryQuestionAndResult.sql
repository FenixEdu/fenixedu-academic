alter table `INQUIRY_QUESTION` add `PRESENT_RESULTS` tinyint(1) default 1;
alter table `INQUIRY_RESULT` add `RESULT_DATE` timestamp NULL default NULL;