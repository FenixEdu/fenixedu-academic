CREATE TABLE `UNIT` (
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	`NAME` varchar(80) NOT NULL default '',
	`COST_CENTER_CODE` int(11) default NULL,
	`KEY_DEPARTMENT` int(11) default NULL,
	`KEY_PARENT_UNIT` int(11) default NULL,
	`ACK_OPT_LOCK` int(11) default NULL,
    PRIMARY KEY (`ID_INTERNAL`)
)TYPE=InnoDB;
