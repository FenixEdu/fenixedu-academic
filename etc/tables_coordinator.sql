
-- ----------------------------
--  Table structure for COORDINATOR
-- ----------------------------
drop table if exists COORDINATOR;
create table COORDINATOR (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_DEGREE int(11) not null,
   KEY_TEACHER int(11) not null,
   RESPONSIBLE bit not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_DEGREE,KEY_TEACHER)
) type=InnoDB;

-- ---------------------------------
--  Table structure for DEGREE_INFO
-- ---------------------------------
drop table if exists DEGREE_INFO;
create table DEGREE_INFO(
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE int(11) not null,
	DESCRIPTION text,
	OBJECTIVES text,
	HISTORY text,
	PROFESSIONAL_EXITS text,
	ADDITIONAL_INFO text,
	LINKS text,
	TEST_INGRESSION text,
	DRIFTS_INITIAL int(11),
	DRIFTS_FIRST int(11),
	DRIFTS_SECOND int(11),
	CLASSIFICATIONS text,
	MARK_MIN float(10,2) ,
	MARK_MAX float(10,2) ,
	MARK_AVERAGE float(10,2),
	DESCRIPTION_EN text,
	OBJECTIVES_EN text,
	HISTORY_EN text,
	PROFESSIONAL_EXITS_EN text,
	ADDITIONAL_INFO_EN text,
	LINKS_EN text,
	TEST_INGRESSION_EN text,	
	CLASSIFICATIONS_EN text,
	LAST_MODIFICATION_DATE datetime,
	primary key (ID_INTERNAL),
	unique U1 (KEY_DEGREE, LAST_MODIFICATION_DATE)
) type=InnoDB;





