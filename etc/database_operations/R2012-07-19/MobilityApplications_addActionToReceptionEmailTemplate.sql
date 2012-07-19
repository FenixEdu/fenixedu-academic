alter table `MOBILITY_EMAIL_TEMPLATE` add `OID_RECEPTION_EMAIL_EXCUTED_ACTION` bigint unsigned;
alter table `EXECUTED_ACTION` add `OID_MOBILITY_EMAIL_TEMPLATE` bigint unsigned;

