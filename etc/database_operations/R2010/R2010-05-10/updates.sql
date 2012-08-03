alter table `FILE` add column `OID_CREATED_BY` bigint(20);
alter table `FILE` add index (`OID_CREATED_BY`);





-- Inserted at 2010-05-07T11:52:13.646+01:00


create table `TEACHER_EVALUATION_CO_EVALUATOR` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `NAME` longtext,
  `OID` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PERSON),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;




-- Inserted at 2010-05-07T16:00:29.045+01:00

alter table `TEACHER_EVALUATION_CO_EVALUATOR` add column `OID_TEACHER_EVALUATION_PROCESS_FROM_EVALUEE` bigint(20);
alter table `TEACHER_EVALUATION_CO_EVALUATOR` add index (`OID_TEACHER_EVALUATION_PROCESS_FROM_EVALUEE`);
alter table TEACHER_EVALUATION_CO_EVALUATOR ADD COLUMN `OJB_CONCRETE_CLASS` varchar(255) default NULL;

-- Inserted at 2010-05-10T11:21:41.109+01:00
alter table `TEACHER_EVALUATION_PROCESS` add column `APPROVED_EVALUATION_MARK` text;
