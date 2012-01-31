CREATE TABLE `PHD_LOG_ENTRY` (
	`OID` bigint unsigned,
	`WHEN_OCCURED` timestamp,
	`STATE` text,
	`MESSAGE` text,
	`ACTIVITY_CLASS_NAME` varchar(255),
	`OID_RESPONSIBLE` bigint unsigned NULL,
	`OID_PHD_PROGRAM_PROCESS` bigint unsigned,
	`OID_ROOT_DOMAIN_OBJECT` bigint unsigned,
	`ID_INTERNAL` int(11) NOT NULL auto_increment PRIMARY KEY,
	index (OID), 
	index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
