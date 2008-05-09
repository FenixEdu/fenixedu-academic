create table DEGREE_CONTEXT (
  `BEGIN_INTERVAL` text not null,
  `END_INTERVAL` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_DEGREE_CURRICULAR_PLAN` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `PERIOD_TYPE` text,
  primary key (ID_INTERNAL),
  index (KEY_DEGREE_CURRICULAR_PLAN),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

