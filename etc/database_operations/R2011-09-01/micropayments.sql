alter table `EVENT` add `OID_INSTITUTION_WHERE_OPEN` bigint unsigned, add `OID_INSTITUTION` bigint unsigned, add index (OID_INSTITUTION), add index (OID_INSTITUTION_WHERE_OPEN);
