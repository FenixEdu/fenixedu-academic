alter table `STUDENT_STATUTE` add `OID_REGISTRATION` bigint unsigned, add `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '';
alter table `REGISTRATION` add `OID_SENIOR_STATUTE` bigint unsigned;