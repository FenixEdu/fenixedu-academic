create table DISTRICT_SUBDIVISION (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `CODE` varchar(2) not null,
  `KEY_DISTRICT` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `NAME` varchar(255) not null,
  primary key (ID_INTERNAL),
  index (KEY_DISTRICT),
  index (KEY_ROOT_DOMAIN_OBJECT),
  unique (KEY_DISTRICT,CODE),
  unique (KEY_DISTRICT,NAME)
) type=InnoDB ;

