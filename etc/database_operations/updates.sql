alter table `DEPARTMENT` add `ACTIVE` tinyint(1);
alter table `EXECUTION_COURSE_AUDIT` add `AVAILABLE_PROCESS` tinyint(1);
alter table `PHD_PROGRAM_INFORMATION` add `NUMBER_OF_SEMESTERS` int(11), add `NUMBER_OF_YEARS` int(11);
alter table `RESOURCE` add `OID_CURRENT_ATTENDANCE` bigint unsigned;
alter table `ALUMNI` add `REGISTERED_WHEN` timestamp NULL default NULL;
alter table `SPACE_ATTENDANCES` add `OID_VISITED_LIBRARY_PLACE` bigint unsigned, add `OID_OCCUPIED_LIBRARY_PLACE` bigint unsigned, add index (OID_VISITED_LIBRARY_PLACE);
