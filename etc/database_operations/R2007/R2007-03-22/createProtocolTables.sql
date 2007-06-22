CREATE TABLE PROTOCOL (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        PROTOCOL_NUMBER VARCHAR(25) NOT NULL default '',
        SIGNED_DATE varchar(50) NOT NULL,
        RENEWABLE tinyint(1) default '0',
        ACTIVE tinyint(1) default '0',
        SCIENTIFIC_AREAS text,
        PROTOCOL_ACTION text,
        OBSERVATIONS text,
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

CREATE TABLE PROTOCOL_HISTORY (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        BEGIN_DATE varchar(50) NOT NULL,
        END_DATE varchar(50) NOT NULL,
		KEY_PROTOCOL int(11) NOT NULL,
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

CREATE TABLE PROTOCOL_RESPONSIBLE (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        KEY_PROTOCOL int(11) NOT NULL default '1',
        KEY_PERSON int(11) NOT NULL default '1',
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

CREATE TABLE PROTOCOL_RESPONSIBLE_PARTNER (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        KEY_PROTOCOL int(11) NOT NULL default '1',
        KEY_PERSON int(11) NOT NULL default '1',
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

CREATE TABLE PROTOCOL_PARTNER (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        KEY_PROTOCOL int(11) NOT NULL default '1',
        KEY_UNIT int(11) NOT NULL default '1',
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

CREATE TABLE PROTOCOL_UNIT (
        ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
        KEY_PROTOCOL int(11) NOT NULL default '1',
        KEY_UNIT int(11) NOT NULL default '1',
        KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
        PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;
