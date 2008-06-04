


-- Inserted at 2008-04-15T17:27:13.397+01:00

alter table ERROR_LOG add index (KEY_EXCEPTION);
alter table ERROR_LOG add index (KEY_ROOT_DOMAIN_OBJECT);
alter table EXCEPTION_TYPE add index (KEY_ROOT_DOMAIN_OBJECT);
alter table REQUEST_LOG add index (KEY_ERROR_LOG);
alter table REQUEST_LOG add index (KEY_MAPPING);
alter table REQUEST_LOG add index (KEY_ROOT_DOMAIN_OBJECT);
alter table REQUEST_MAPPING add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_CURRICULAR_LOAD add index (KEY_ROOT_DOMAIN_OBJECT);


create table PROCESS (
  `CYCLE_TYPE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_DEGREE` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `OJB_CONCRETE_CLASS` text,
  primary key (ID_INTERNAL),
  index (KEY_DEGREE),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

create table PROCESS_LOG (
  `ACTIVITY` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PROCESS` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `USER_NAME` text,
  `WHEN_DATE_TIME` datetime default NULL,
  primary key (ID_INTERNAL),
  index (KEY_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

