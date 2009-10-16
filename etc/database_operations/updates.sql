


-- Inserted at 2009-09-22T15:37:16.819+01:00

alter table `INDIVIDUAL_CANDIDACY` add column `UTL_STUDENT` tinyint(1);
alter table `PAYMENT_CODE` add column `KEY_PERSON` int(11);
alter table `PAYMENT_CODE` add column `OID_PERSON` bigint(20);
alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
alter table `PHD_ALERT_MESSAGE` add column `KEY_PERSON_WHO_MARKED_AS_READED` int(11);
alter table `PHD_ALERT_MESSAGE` add column `OID_PERSON_WHO_MARKED_AS_READED` bigint(20);
alter table `PHD_ALERT_MESSAGE` add column `READED` tinyint(1);
alter table `PHD_ALERT_MESSAGE` add index (`KEY_PERSON_WHO_MARKED_AS_READED`), add index (`OID_PERSON_WHO_MARKED_AS_READED`);
alter table `PROCESS` add column `WHEN_FORMALIZED_REGISTRATION` text;
alter table `PROCESS` add column `WHEN_STARTED_STUDIES` text;
alter table `REQUEST_LOG` add column `POST` tinyint(1);
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `SECOND_CYCLE_INDIVIDUAL_CANDIDACY_AMOUNT` text;


create table `PHD_ALERT_MESSAGE_PERSON` (KEY_PERSON int(11) not null, KEY_PHD_ALERT_MESSAGE int(11) not null, OID_PERSON bigint unsigned default null, OID_PHD_ALERT_MESSAGE bigint unsigned default null,  primary key (KEY_PERSON, KEY_PHD_ALERT_MESSAGE), key(KEY_PERSON), key(KEY_PHD_ALERT_MESSAGE), key (OID_PERSON, OID_PHD_ALERT_MESSAGE), key(OID_PERSON), key(OID_PHD_ALERT_MESSAGE)) type=InnoDB;



-- Inserted at 2009-10-06T16:24:56.746+01:00

alter table `INDIVIDUAL_CANDIDACY` add column `UTL_STUDENT` tinyint(1);
alter table `PAYMENT_CODE` add column `KEY_PERSON` int(11);
alter table `PAYMENT_CODE` add column `OID_PERSON` bigint(20);
alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
alter table `PHD_ALERT_MESSAGE` add column `KEY_PERSON_WHO_MARKED_AS_READED` int(11);
alter table `PHD_ALERT_MESSAGE` add column `OID_PERSON_WHO_MARKED_AS_READED` bigint(20);
alter table `PHD_ALERT_MESSAGE` add column `READED` tinyint(1);
alter table `PHD_ALERT_MESSAGE` add index (`KEY_PERSON_WHO_MARKED_AS_READED`), add index (`OID_PERSON_WHO_MARKED_AS_READED`);
alter table `PROCESS` add column `WHEN_FORMALIZED_REGISTRATION` text;
alter table `PROCESS` add column `WHEN_STARTED_STUDIES` text;
alter table `REQUEST_LOG_DAY` add column `KEY_NEXT` int(11);
alter table `REQUEST_LOG_DAY` add column `KEY_PREVIOUS` int(11);
alter table `REQUEST_LOG_DAY` add column `OID_NEXT` bigint(20);
alter table `REQUEST_LOG_DAY` add column `OID_PREVIOUS` bigint(20);
alter table `REQUEST_LOG_DAY` add index (`KEY_NEXT`), add index (`OID_NEXT`);
alter table `REQUEST_LOG_DAY` add index (`KEY_PREVIOUS`), add index (`OID_PREVIOUS`);
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `SECOND_CYCLE_INDIVIDUAL_CANDIDACY_AMOUNT` text;


create table `PHD_ALERT_MESSAGE_PERSON` (KEY_PERSON int(11) not null, KEY_PHD_ALERT_MESSAGE int(11) not null, OID_PERSON bigint unsigned default null, OID_PHD_ALERT_MESSAGE bigint unsigned default null,  primary key (KEY_PERSON, KEY_PHD_ALERT_MESSAGE), key(KEY_PERSON), key(KEY_PHD_ALERT_MESSAGE), key (OID_PERSON, OID_PHD_ALERT_MESSAGE), key(OID_PERSON), key(OID_PHD_ALERT_MESSAGE)) type=InnoDB;



-- Inserted at 2009-10-06T17:23:52.574+01:00

alter table `INDIVIDUAL_CANDIDACY` add column `UTL_STUDENT` tinyint(1);
alter table `PAYMENT_CODE` add column `KEY_PERSON` int(11);
alter table `PAYMENT_CODE` add column `OID_PERSON` bigint(20);
alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
alter table `PHD_ALERT_MESSAGE` add column `KEY_PERSON_WHO_MARKED_AS_READED` int(11);
alter table `PHD_ALERT_MESSAGE` add column `OID_PERSON_WHO_MARKED_AS_READED` bigint(20);
alter table `PHD_ALERT_MESSAGE` add column `READED` tinyint(1);
alter table `PHD_ALERT_MESSAGE` add index (`KEY_PERSON_WHO_MARKED_AS_READED`), add index (`OID_PERSON_WHO_MARKED_AS_READED`);
alter table `PROCESS` add column `WHEN_FORMALIZED_REGISTRATION` text;
alter table `PROCESS` add column `WHEN_STARTED_STUDIES` text;
alter table `REQUEST_LOG_DAY` add column `KEY_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY` int(11);
alter table `REQUEST_LOG_DAY` add column `OID_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY` bigint(20);
alter table `REQUEST_LOG_DAY` add index (`KEY_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY`), add index (`OID_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY`);
alter table `ROOT_DOMAIN_OBJECT` add column `KEY_MOST_RECENT_REQUEST_LOG_DAY` int(11);
alter table `ROOT_DOMAIN_OBJECT` add column `OID_MOST_RECENT_REQUEST_LOG_DAY` bigint(20);
alter table `ROOT_DOMAIN_OBJECT` add index (`KEY_MOST_RECENT_REQUEST_LOG_DAY`), add index (`OID_MOST_RECENT_REQUEST_LOG_DAY`);
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `SECOND_CYCLE_INDIVIDUAL_CANDIDACY_AMOUNT` text;


create table `PHD_ALERT_MESSAGE_PERSON` (KEY_PERSON int(11) not null, KEY_PHD_ALERT_MESSAGE int(11) not null, OID_PERSON bigint unsigned default null, OID_PHD_ALERT_MESSAGE bigint unsigned default null,  primary key (KEY_PERSON, KEY_PHD_ALERT_MESSAGE), key(KEY_PERSON), key(KEY_PHD_ALERT_MESSAGE), key (OID_PERSON, OID_PHD_ALERT_MESSAGE), key(OID_PERSON), key(OID_PHD_ALERT_MESSAGE)) type=InnoDB;



-- Inserted at 2009-10-07T16:57:23.348+01:00

alter table `INDIVIDUAL_CANDIDACY` add column `UTL_STUDENT` tinyint(1);
alter table `PAYMENT_CODE` add column `KEY_PERSON` int(11);
alter table `PAYMENT_CODE` add column `OID_PERSON` bigint(20);
alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
alter table `PENDING_REQUEST` add column `BUILD_VERSION` text;
alter table `PHD_ALERT_MESSAGE` add column `KEY_PERSON_WHO_MARKED_AS_READED` int(11);
alter table `PHD_ALERT_MESSAGE` add column `OID_PERSON_WHO_MARKED_AS_READED` bigint(20);
alter table `PHD_ALERT_MESSAGE` add column `READED` tinyint(1);
alter table `PHD_ALERT_MESSAGE` add index (`KEY_PERSON_WHO_MARKED_AS_READED`), add index (`OID_PERSON_WHO_MARKED_AS_READED`);
alter table `PROCESS` add column `WHEN_FORMALIZED_REGISTRATION` text;
alter table `PROCESS` add column `WHEN_STARTED_STUDIES` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_AMOUNT` text;
alter table `SIBS_PAYMENT_FILE_PROCESS_REPORT` add column `SECOND_CYCLE_INDIVIDUAL_CANDIDACY_AMOUNT` text;


create table `PHD_ALERT_MESSAGE_PERSON` (KEY_PERSON int(11) not null, KEY_PHD_ALERT_MESSAGE int(11) not null, OID_PERSON bigint unsigned default null, OID_PHD_ALERT_MESSAGE bigint unsigned default null,  primary key (KEY_PERSON, KEY_PHD_ALERT_MESSAGE), key(KEY_PERSON), key(KEY_PHD_ALERT_MESSAGE), key (OID_PERSON, OID_PHD_ALERT_MESSAGE), key(OID_PERSON), key(OID_PHD_ALERT_MESSAGE)) type=InnoDB;
