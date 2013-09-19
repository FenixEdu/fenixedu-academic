alter table `SANTANDER_ENTRY` add `OID_SANTANDER_PHOTO_ENTRY` bigint unsigned;
alter table `SANTANDER_PHOTO_ENTRY` add `OID_PHOTOGRAPH` bigint unsigned, add `OID_SANTANDER_ENTRY` bigint unsigned, add index (OID_PHOTOGRAPH);
