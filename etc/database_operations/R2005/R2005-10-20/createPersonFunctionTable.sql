CREATE TABLE `PERSON_FUNCTION` (
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	`BEGIN_DATE` date default NULL,
	`END_DATE` date default NULL,
	`CREDITS` int(10) default NULL,
	`KEY_PERSON` int(11) default NULL,
	`KEY_FUNCTION` int(11) default NULL,
	`ACK_OPT_LOCK` int(11) default NULL,
    PRIMARY KEY (`ID_INTERNAL`)
)TYPE=InnoDB;