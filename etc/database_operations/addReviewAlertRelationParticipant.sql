alter table `PHD_PARTICIPANT` add `OID_PHD_REPORTER_REVIEW_ALERT` bigint unsigned;
alter table `ALERT` add `OID_PHD_PARTICIPANT` bigint unsigned;
