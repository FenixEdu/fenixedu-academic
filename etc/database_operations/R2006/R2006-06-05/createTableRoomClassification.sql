create table ROOM_CLASSIFICATION (
	ID_INTERNAL int(11) not null auto_increment,
	CODE varchar(50),
	NAME varchar(255),
	KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
	primary key (ID_INTERNAL),
	unique (CODE),
	index KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
