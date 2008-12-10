


-- Inserted at 2008-12-10T17:48:38.285Z

alter table ALUMNI_IDENTITY_CHECK_REQUEST add column COMMENT text;
alter table EXEMPTION_JUSTIFICATION add column DISPATCH_DATE text;
alter table EXEMPTION_JUSTIFICATION add column JUSTIFICATION_TYPE text;


create table QUEUE_JOB_CONTEXT (
  `CALLBACK_SERVICE` text,
  `DONE` tinyint(1),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `JOB_END_TIME` timestamp NULL default NULL,
  `JOB_START_TIME` timestamp NULL default NULL,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE` int(11),
  `KEY_USER` int(11),
  `REQUEST_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE),
  index (KEY_USER)
) type=InnoDB ;

