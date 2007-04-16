create table ROLE (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	ROLE_TYPE varchar(50) NOT NULL,
	PORTAL_SUB_APPLICATION varchar(100) default NULL,
	PAGE varchar(100) default NULL,
	PAGE_NAME_PROPERTY varchar(100) default NULL,
	ACK_OPT_LOCK int(11) default NULL,
	KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
	PRIMARY KEY (ID_INTERNAL),
	UNIQUE KEY (ROLE_TYPE),
	KEY (KEY_ROOT_DOMAIN_OBJECT)
) TYPE=InnoDB;

create table ROOT_DOMAIN_OBJECT (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_ROOT_DOMAIN_OBJECT int(11) default null,
	KEY_EXTERNAL_INSTITUTION_UNIT int(11) default NULL,
	KEY_INSTITUTION_UNIT int(11) default NULL,
	KEY_EARTH_UNIT int(11) default NULL,
	KEY_USERNAME_COUNTER int(11) default NULL,
	primary key (ID_INTERNAL),
	index (KEY_EXTERNAL_INSTITUTION_UNIT),
	index (KEY_INSTITUTION_UNIT),
	index (KEY_EARTH_UNIT),
	index (KEY_USERNAME_COUNTER)
) TYPE=InnoDB;

insert into ROOT_DOMAIN_OBJECT values (1, null, null, null, null);
