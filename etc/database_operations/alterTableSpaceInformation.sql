drop table SPACE_INFORMATION;
create table SPACE_INFORMATION (
	ID_INTERNAL int(11) not null auto_increment,
	OJB_CONCRETE_CLASS varchar(250) not null,
	VALID_UNTIL varchar(10) default NULL,
	KEY_SPACE int(11) not null,
	BLUEPRINT_NUMBER int(11) default null,
	IDENTIFICATION varchar(250) default null,
	DESCRIPTION varchar(250) default null,
	CLASSIFICATION varchar(50) default null,
	AREA varchar(250) default null,
	HEIGHT_QUALITY tinyint(1) default null,
	ILLUMINATION_QUALITY tinyint(1) default null,
	DISTANCE_FROM_SANITARY_INSTALATIONS_QUALITY tinyint(1) default null,
	SECURITY_QUALITY tinyint(1) default null,
	AGE_QUALITY tinyint(1) default null,
	OBSERVATIONS varchar(250) default null,
	NAME varchar(255) default NULL,
	LEVEL int(11) default NULL,
	KEY_ROOT_DOMAIN_OBJECT int(11) not null default '1',
	primary key (ID_INTERNAL),
	index KEY_SPACE (KEY_SPACE),
	index KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
