DROP TABLE IF EXISTS MONEY_COST_CENTER;
CREATE TABLE MONEY_COST_CENTER(
	ID_INTERNAL int(11) NOT NULL auto_increment,
  	INITIAL_VALUE float default '0',
  	TOTAL_VALUE float default '0',
  	SPENT_VALUE float default '0',
	YEAR int(11) NOT NULL,
  	KEY_COST_CENTER int(11) NOT NULL,  
	KEY_EMPLOYEE int(11) unsigned default NULL,
	WHEN_ALTER timestamp(14) NOT NULL,
	ACK_OPT_LOCK int(11) default NULL,
    PRIMARY KEY  (ID_INTERNAL),
    UNIQUE KEY U1 (KEY_COST_CENTER, YEAR)
) TYPE=InnoDB;

DROP TABLE IF EXISTS EXTRA_WORK;
CREATE TABLE EXTRA_WORK(
	ID_INTERNAL int(11) NOT NULL auto_increment,
	DAY date NOT NULL,
	BEGIN_HOUR time NOT NULL,
	END_HOUR time NOT NULL,
  	MEAL_SUBSIDY int(11) default NULL,
  	DIURNAL_FIRST_HOUR time default NULL,
  	DIURNAL_AFTER_SECOND_HOUR time default NULL,
  	NOCTURNAL_FIRST_HOUR time default NULL,
  	NOCTURNAL_AFTER_SECOND_HOUR time default NULL,
  	REST_DAY time default NULL,
  	MEAL_SUBSIDY_AUTHORIZED tinyint(1) NOT NULL default '0',
  	DIURNAL_FIRST_HOUR_AUTHORIZED tinyint(1) NOT NULL default '0',
  	DIURNAL_AFTER_SECOND_HOUR_AUTHORIZED tinyint(1) NOT NULL default '0',
  	NOCTURNAL_FIRST_HOUR_AUTHORIZED tinyint(1) NOT NULL default '0',
  	NOCTURNAL_AFTER_SECOND_HOUR_AUTHORIZED tinyint(1) NOT NULL default '0',
  	REST_DAY_AUTHORIZED tinyint(1) NOT NULL default '0',	  	
  	DAY_PER_WEEK tinyint(1) NOT NULL default '0',
  	HOLIDAY tinyint(1) NOT NULL default '0',
  	REMUNERATION tinyint(1) NOT NULL default '0', 	
	KEY_EMPLOYEE int(11) unsigned default NULL,
	KEY_EMPLOYEE_DB int(11) unsigned default NULL,
	WHEN_ALTER timestamp(14) NOT NULL,
	ACK_OPT_LOCK int(11) default NULL,
    PRIMARY KEY  (ID_INTERNAL),
    UNIQUE KEY U1 (KEY_EMPLOYEE, DAY)
) TYPE=InnoDB;

DROP TABLE IF EXISTS EXTRA_WORK_REQUESTS;
CREATE TABLE EXTRA_WORK_REQUESTS(
	ID_INTERNAL int(11) NOT NULL auto_increment,
	BEGIN_DATE date NOT NULL,
	END_DATE date NOT NULL,	
	OPTION_1 tinyint(1),
	OPTION_2 tinyint(1),
	OPTION_3 tinyint(1),
	OPTION_4 tinyint(1),
	OPTION_5 tinyint(1),
	OPTION_6 tinyint(1),
	OPTION_7 tinyint(1),
	OPTION_8 tinyint(1),
	OPTION_9 tinyint(1),
	OPTION_10 tinyint(1),
	OPTION_11 tinyint(1),
	OPTION_12 tinyint(1),			
	KEY_EMPLOYEE int(11) unsigned default NULL,
  	KEY_COST_CENTER_EXTRA_WORK int(11) NOT NULL,
  	KEY_COST_CENTER_MONEY int(11) NOT NULL,
	KEY_EMPLOYEE_DB int(11) unsigned default NULL,
	WHEN_ALTER timestamp(14) NOT NULL,
	ACK_OPT_LOCK int(11) default NULL,
    PRIMARY KEY  (ID_INTERNAL),
    UNIQUE KEY U1 (KEY_EMPLOYEE, BEGIN_DATE, END_DATE, KEY_COST_CENTER_MONEY)
) TYPE=InnoDB;

DROP TABLE IF EXISTS EXTRA_WORK_COMPENSATION;
-- CREATE TABLE EXTRA_WORK_COMPENSATION(
--	ID_INTERNAL int(11) NOT NULL auto_increment,
--	BEGIN_DATE date NOT NULL,
--	END_DATE date NOT NULL,	
-- 	DAY_PER_WEEK tinyint(1) NOT NULL default '0',
--  	HOLIDAY tinyint(1) NOT NULL default '0',
--  	REMUNERATION tinyint(1) NOT NULL default '0', 	
--	KEY_EMPLOYEE int(11) unsigned default NULL,
--  	KEY_EMPLOYEE_DB int(11) unsigned default NULL,
--	WHEN_ALTER timestamp(14) NOT NULL,
--	ACK_OPT_LOCK int(11) default NULL,
--    PRIMARY KEY  (ID_INTERNAL),
--    UNIQUE KEY U1 (KEY_EMPLOYEE, BEGIN_DATE, END_DATE)
--) TYPE=InnoDB;

DROP TABLE IF EXISTS EXTRA_WORK_HISTORIC;
CREATE TABLE EXTRA_WORK_HISTORIC(
	ID_INTERNAL int(11) NOT NULL auto_increment,
	YEAR int(11) NOT NULL,	
	SERVICE_DISMISSAL_PER_YEAR int(11) unsigned default NULL,   
	HOLIDAYS_NUMBER_PER_YEAR int(11) unsigned default NULL,
	SERVICE_DISMISSAL time default NULL,   
	HOLIDAYS_NUMBER time default NULL,
	HOURS_EXTRAWORK_PER_YEAR time default NULL,	  	
	HOURS_EXTRAWORK_PER_DAY time default NULL,	  	
	KEY_EMPLOYEE int(11) unsigned default NULL,
  	KEY_EMPLOYEE_DB int(11) unsigned default NULL,
	WHEN_ALTER timestamp(14) NOT NULL,
	ACK_OPT_LOCK int(11) default NULL,
    PRIMARY KEY  (ID_INTERNAL),
    UNIQUE KEY U1 (KEY_EMPLOYEE, YEAR)
) TYPE=InnoDB;
