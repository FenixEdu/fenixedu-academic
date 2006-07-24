alter table HOMEPAGE add column KEY_BLOG int(11) default NULL;

create table BLOG (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_HOMEPAGE int(11) NOT NULL,
	NAME varchar(150) NOT NULL,
	ABOUT text default NULL,
	PRIMARY KEY  (ID_INTERNAL),
	UNIQUE KEY (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table BLOG_ENTRY (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_BLOG int(11) NOT NULL,
	POSTED timestamp default 0,
	TITLE varchar(255) NOT NULL,
	CONTENT text default NULL,
	PRIMARY KEY  (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table BLOG_ENTRY_COMMENT (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_BLOG_ENTRIES int(11) NOT NULL,
	POSTED timestamp default 0,
	COMMENT varchar(255) NOT NULL,
	PRIMARY KEY  (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
