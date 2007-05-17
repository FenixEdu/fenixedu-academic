CREATE TABLE ASSIDUOUSNESS_VACATIONS (
    ID_INTERNAL int(11) not null auto_increment,
    KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
    KEY_ASSIDUOUSNESS int(11) not null,
    YEAR int(4) not null,
    NORMAL_DAYS double NOT NULL default '0',
    NORMAL_WITH_LEAVES_DISCOUNT double NOT NULL default '0',
	ANTIQUITY_DAYS double NOT NULL default '0',
	AGE_DAYS double NOT NULL default '0',
	ACCUMULATED_DAYS double NOT NULL default '0',
	BONUS_DAYS double NOT NULL default '0',
	LOW_TIME_VACATIONS_DAYS double NOT NULL default '0',
	ARTICLE17_DAYS double NOT NULL default '0',
	LAST_MODIFIED_DATE DATETIME,
    primary key (ID_INTERNAL),
    index (KEY_ROOT_DOMAIN_OBJECT),
    index (KEY_ASSIDUOUSNESS),
    UNIQUE KEY `u1` (`KEY_ASSIDUOUSNESS`,`YEAR`)
) Type=InnoDB;


ALTER TABLE ass_FUNCIONARIO DROP COLUMN ANTIQUITY_YEAR_MONTH_DAY;
