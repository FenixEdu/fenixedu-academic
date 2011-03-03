alter table `INQUIRY_RESULT` add `SCALE_VALUE` text;
alter table `INQUIRY_ANSWER` add `ALLOW_ACADEMIC_PUBLICIZING` tinyint(1);
alter table `INQUIRY_ANSWER` add `OID_DELEGATE` bigint unsigned, add index (OID_DELEGATE);
alter table `INQUIRY_BLOCK` add `OID_RESULT_QUESTION` bigint unsigned, add index (OID_RESULT_QUESTION);
alter table `INQUIRY_ANSWER` add `LAST_MODIFIED_DATE` timestamp NULL default NULL;
alter table `INQUIRY_QUESTION` add `HAS_CLASSIFICATION` tinyint(1) default 0;