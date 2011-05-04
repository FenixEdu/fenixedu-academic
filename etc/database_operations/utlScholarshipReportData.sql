create table `U_T_L_SCHOLARSHIP_REPORT_STUDENT` (`OID_STUDENT` bigint unsigned, `OID_U_T_L_SCHOLARSHIP_REPORT` bigint unsigned, primary key (OID_STUDENT, OID_U_T_L_SCHOLARSHIP_REPORT), index (OID_STUDENT), index (OID_U_T_L_SCHOLARSHIP_REPORT)) ENGINE=InnoDB, character set latin1;
alter table `FILE` add `OID_UTL_SCHOLARSHIP_REPORT` bigint unsigned;
alter table `QUEUE_JOB` add `OID_UTL_SCHOLARSHIP_SOURCE` bigint unsigned;
