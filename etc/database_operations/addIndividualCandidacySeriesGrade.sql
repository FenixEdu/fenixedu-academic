


-- Inserted at 2012-07-16T12:22:41.898+01:00

create table `INDIVIDUAL_CANDIDACY_SERIES_GRADE` (
	`AFFINITY` text, 
	`INTERVIEW_GRADE` text, 
	`OID` bigint unsigned, 
	`PROFESSIONAL_STATUS` text, 
	`OID_DEGREE` bigint unsigned, 
	`APPROVED_ECTS_RATE` text, 
	`SERIES_CANDIDACY_GRADE` text, 
	`OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', 
	`DEGREE_NATURE` int(11), 
	`OID_INDIVIDUAL_CANDIDACY` bigint unsigned, 
	`CANDIDACY_GRADE` text, 
	`PROFESSIONAL_EXPERIENCE` int(11), 
	`GRADE_RATE` text, 
	`OTHER_EDUCATION` text, 
	`OID_ROOT_DOMAIN_OBJECT` bigint unsigned, 
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	primary key (ID_INTERNAL), 
	index (OID), 
	index (OID_DEGREE), 
	index (OID_INDIVIDUAL_CANDIDACY), 
	index (OID_ROOT_DOMAIN_OBJECT)
) ENGINE=InnoDB, character set latin1;
