alter table `FILE` add `OID_PHD_PARTICIPANT` bigint unsigned, add index (OID_PHD_PARTICIPANT);
alter table `PHD_PARTICIPANT` add `OID_ACCEPTANCE_LETTER` bigint unsigned;
