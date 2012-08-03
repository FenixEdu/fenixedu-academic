alter table `SPACE_INFORMATION` add `CAPACITY` int(11);
alter table `RESOURCE` add `OID_ROOT_DOMAIN_OBJECT_FOR_LIBRARY` bigint unsigned, add index (OID_ROOT_DOMAIN_OBJECT_FOR_LIBRARY);
