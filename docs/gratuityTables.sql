----------------------------
-- Table structure for GRATUITY_VALUES
----------------------------
DROP TABLE IF EXISTS GRATUITY_VALUES;
CREATE TABLE GRATUITY_VALUES (
	ID_INTERNAL int(11) not null auto_increment,
	ANUAL_VALUE float, 
	SCHOLARSHIP_VALUE float,
	FINAL_PROOF_VALUE float,  
	COURSE_VALUE  float,
	CREDIT_VALUE  float,
	PROOF_REQUEST_PAYMENT tinyint(1),
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
	PAYED_VALUE float,
	REMAINING_VALUE float,
	EXEMPTION_PERCENTAGE int(11),
	EXEMPTION_TYPE int(11),
	EXEMPTION_DESCRIPTION varchar(50), 
	KEY_GRATUITY int(11),
	KEY_STUDENT_CURRICULAR_PLAN int(11),
	ACK_OPT_LOCK int(11) default NULL,
	primary key (ID_INTERNAL)
) type=InnoDB;

DROP TABLE IF EXISTS PAYMENT_PHASE;
CREATE TABLE PAYMENT_PHASE(
	ID_INTERNAL int(11) not null auto_increment,
	START_PAYMENT date,
	END_PAYMENT date,
	VALUE float,
	DESCRIPTION varchar(50), 
	KEY_GRATUITY int(11),
	primary key (ID_INTERNAL)
) type=InnoDB;


