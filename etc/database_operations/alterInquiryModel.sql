alter table `INQUIRY_QUESTION` add `IS_MATRIX` tinyint(1);
update `INQUIRY_QUESTION` set `IS_MATRIX` = 1 where `IS_MATRIX` is null;
alter table `REGISTRATION` add `OID_INQUIRY_STUDENT_CYCLE_ANSWER` bigint unsigned;
alter table `INQUIRY_ANSWER` add `OID_REGISTRATION` bigint unsigned;