alter table THESIS add column KEY_LAST_LIBRARY_OPERATION int(11);

create table THESIS_LIBRARY_OPERATION (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_NEXT` int(11),
  `KEY_PERFORMER` int(11),
  `KEY_PREVIOUS` int(11),
  `KEY_THESIS` int(11),
  `LIBRARY_REFERENCE` text,
  `OJB_CONCRETE_CLASS` text,
  `OPERATION` timestamp NULL default NULL,
  `PENDING_COMMENT` text,
  primary key (ID_INTERNAL)
) type=InnoDB ;
