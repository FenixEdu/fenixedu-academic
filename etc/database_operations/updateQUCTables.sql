alter table `INQUIRY_QUESTION_HEADER` add `OID_RESULT_GROUP_QUESTION` bigint unsigned;
alter table `INQUIRY_GROUP_QUESTION` add `OID_RESULT_QUESTION_HEADER` bigint unsigned;
alter table `INQUIRY_RESULT_COMMENT` drop ORDER, add `RESULT_ORDER` int(11);
alter table `INQUIRY_BLOCK` add `OID_GROUP_RESULT_QUESTION` bigint unsigned, add index (OID_GROUP_RESULT_QUESTION);