CREATE TABLE `PAYMENT_TRANSACTION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `ACK_OPT_LOCK` int(11) default NULL,
  `VALUE` float NOT NULL default '0',
  `TRANSACTION_DATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `REMARKS` text,
  `PAYMENT_TYPE` varchar(50) default NULL,
  `TRANSACTION_TYPE` varchar(50) default NULL,
  `WAS_INTERNAL_BALANCE` int(1) NOT NULL default '0',
  `KEY_RESPONSIBLE_PERSON` int(11) NOT NULL default '0',
  `KEY_GUIDE_ENTRY` int(11) default NULL,
  `KEY_PERSON_ACCOUNT` int(11) NOT NULL default '0',
  `KEY_GRATUITY_SITUATION` int(11) NOT NULL default '0', 
  `KEY_EXECUTION_YEAR` int(11) NOT NULL default '0',
  `KEY_STUDENT` int(11) NOT NULL default '0',
  `CLASS_NAME` varchar(255) NOT NULL default 'net.sourceforge.fenixedu.domain.transactions.PaymentTransaction', 
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `KEY_GUIDE_ENTRY` (`KEY_GUIDE_ENTRY`)
) TYPE=InnoDB;
