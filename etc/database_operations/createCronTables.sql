create table CRON_SCRIPT_STATE (
	ID_INTERNAL int(11) not null auto_increment,
	KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
	CRON_SCRIPT_CLASSNAME text not null,
	ABSOLUTE_EXECUTION_ORDER int(11) not null,
	REGISTRATION_DATE date not null,
	IS_CURRENTLY_RUNNING tinyint(1) not null default 0,
	CONTEXT blob default null,
	primary key (ID_INTERNAL),
	index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;

create table CRON_SCRIPT_INVOCATION (
	ID_INTERNAL int(11) not null auto_increment,
	KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
	KEY_CRON_SCRIPT_STATE int(11) not null,
	SERVER_I_D text not null,
	START_TIME date not null,
	END_TIME date not null,
	SUCCESSFUL tinyint(1) not null default 0,
	primary key (ID_INTERNAL),
	index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
