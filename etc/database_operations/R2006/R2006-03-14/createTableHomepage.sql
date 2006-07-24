create table HOMEPAGE (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_PERSON int(11) NOT NULL,
	MY_URL varchar(150) NOT NULL,
	PRIMARY KEY  (ID_INTERNAL),
	UNIQUE KEY (MY_URL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table PARTY add column KEY_HOMEPAGE int(11) default null;
