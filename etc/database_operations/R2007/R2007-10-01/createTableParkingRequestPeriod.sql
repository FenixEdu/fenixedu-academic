create table PARKING_REQUEST_PERIOD (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  `BEGIN_DATE` timestamp NULL default NULL,
  `END_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL)
) type=InnoDB ;

