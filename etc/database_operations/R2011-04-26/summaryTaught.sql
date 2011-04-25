alter table `SUMMARY` add `TAUGHT` tinyint(1);
update SUMMARY set TAUGHT = TRUE;
