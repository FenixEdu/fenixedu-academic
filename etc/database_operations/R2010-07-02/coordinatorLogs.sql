create table `COORDINATOR_LOG` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_EXECUTION_DEGREE` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_PERSON_WHO` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OPERATION` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_EXECUTION_DEGREE),
  index (OID_PERSON),
  index (OID_PERSON_WHO),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;



-- Inserted at 2010-05-14T18:28:23.339+01:00

alter table `COORDINATOR_LOG` add column `DATE` timestamp NULL default NULL;
