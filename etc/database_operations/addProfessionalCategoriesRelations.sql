alter table `T_S_D_TEACHER` add `OID_PROFESSIONAL_CATEGORY` bigint unsigned, add index (OID_PROFESSIONAL_CATEGORY);
alter table `PROFESSIONAL_CATEGORY` add `WEIGHT` int(11);
alter table `PROFESSIONAL_SITUATION` add `OID_PROFESSIONAL_CATEGORY` bigint unsigned, add index (OID_PROFESSIONAL_CATEGORY);
alter table `CAREER` add `CATEGORY_NAME` text;