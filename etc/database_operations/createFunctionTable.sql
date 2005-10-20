CREATE TABLE `FUNCTION` (
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	`NAME` varchar(100) NOT NULL default '',
	`KEY_UNIT` int(11) default NULL,
	`KEY_INHERENT_PARENT_FUNCTION` int(11) default NULL,
	`TYPE` varchar(80) default 'UNKNOWN',
	`ACK_OPT_LOCK` int(11) default NULL,
    PRIMARY KEY (`ID_INTERNAL`)
)TYPE=InnoDB;