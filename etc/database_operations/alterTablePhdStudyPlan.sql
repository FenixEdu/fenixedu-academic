
alter table PHD_STUDY_PLAN add column EXEMPTED tinyint(1);
alter table PHD_STUDY_PLAN change column KEY_DEGREE KEY_DEGREE int(11) DEFAULT NULL, change column OID_DEGREE OID_DEGREE bigint(20) DEFAULT NULL;
 