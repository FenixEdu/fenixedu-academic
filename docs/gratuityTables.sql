----------------------------
-- Table structure for GRATUITY
----------------------------
DROP TABLE IF EXISTS GRATUITY;
CREATE TABLE GRATUITY (
	ID_INTERNAL int(11) not null auto_increment,
	ANUAL_VALUE float not null, 
	SCHOLARSHIP_VALUE float not null,
	FINAL_PROOF_VALUE float not null,  
	COURSE_VALUE  float,
	CREDIT_VALUE  float,
	PROOF_REQUEST_PAYMENT tinyint(1) default '0',
	START_PAYMENT date,
	END_PAYMENT date,
	KEY_EXECUTION_DEGREE int(11),
	KEY_EMPLOYEE int(11),
	ACK_OPT_LOCK int(11) default NULL,
	primary key (ID_INTERNAL)
) type=InnoDB;

DROP TABLE IF EXISTS GRATUITY_SITUATION;
CREATE TABLE GRATUITY_SITUATION(
	ID_INTERNAL int(11) not null auto_increment,
	PAYED_VALUE float not null,
	REMAINING_VALUE float default 0.0,
	EXEMPTION_PERCENTAGE int(11),
	EXEMPTION_TYPE int(11),
	EXEMPTION_DESCRIPTION varchar(50), 
	KEY_GRATUITY int(11),
	KEY_STUDENT int(11),
	ACK_OPT_LOCK int(11) default NULL,
	primary key (ID_INTERNAL)
) type=InnoDB;

DROP TABLE IF EXISTS PAYMENT_PHASE;
CREATE TABLE PAYMENT_PHASE(
	ID_INTERNAL int(11) not null auto_increment,
	START_PAYMENT date,
	END_PAYMENT date,
	VALUE float not null,
	DESCRIPTION varchar(50), 
	KEY_GRATUITY int(11),
	primary key (ID_INTERNAL)
) type=InnoDB;


