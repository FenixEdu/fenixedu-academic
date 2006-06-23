CREATE TABLE `CONTRACT` (
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	`KEY_CATEGORY` int(11) default NULL,
	`KEY_MAILING_UNIT` int(11) default NULL,
	`KEY_SALARY_UNIT` int(11) default NULL,
	`KEY_WORKING_UNIT` int(11) default NULL,
	`KEY_EMPLOYEE` int(11) default NULL,
	`BEGIN_DATE` date default NULL,
	`END_DATE` date default NULL,
	`ACK_OPT_LOCK` int(11) default NULL,
    PRIMARY KEY (`ID_INTERNAL`)
)TYPE=InnoDB;