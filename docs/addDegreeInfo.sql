-- ---------------------------------
--Table structure for DEGREE_INFO
-- ---------------------------------
drop table if exists DEGREE_INFO;
create table DEGREE_INFO(
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE int(11) not null,
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
	MARK_AVERAGE float(10,2) ,
	LAST_MODIFICATION_DATE datetime,
	primary key (ID_INTERNAL),
	unique U1 (KEY_DEGREE, LAST_MODIFICATION_DATE)
) type=InnoDB;



