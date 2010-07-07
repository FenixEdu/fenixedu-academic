alter table `FILE` add `SUCCESSFUL_SENT_DATE` timestamp NULL default NULL, add `ERRORS` text, add `OID_EXECUTION_YEAR` bigint(20);
alter table `QUEUE_JOB` add `LAST_SUCCESSFUL_SENT_PAYMENT_FILE_DATE` timestamp NULL default NULL;
