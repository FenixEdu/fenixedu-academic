
create table `COORDINATOR_EXECUTION_DEGREE_COURSES_REPORT` (
  `BOLONHA_PROCESS_IMPLEMENTATION_REFLECTION` longtext,
  `GLOBAL_COMMENT` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_COORDINATOR` int(11), OID_COORDINATOR bigint unsigned default null,
  `KEY_EXECUTION_DEGREE` int(11), OID_EXECUTION_DEGREE bigint unsigned default null,
  `KEY_EXECUTION_INTERVAL` int(11), OID_EXECUTION_INTERVAL bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `STEPS_TO_IMPROVE_RESULTS` longtext,
  `TEACHING_BEST_PRACTICES_DEVELOPED_BY_TEACHERS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_COORDINATOR),
  index (OID_COORDINATOR),
  index (KEY_EXECUTION_DEGREE),
  index (OID_EXECUTION_DEGREE),
  index (KEY_EXECUTION_INTERVAL),
  index (OID_EXECUTION_INTERVAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
