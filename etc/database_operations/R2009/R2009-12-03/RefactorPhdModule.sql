rename table PHD_PROGRAM_GUIDING to PHD_PARTICIPANT;

update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.PhdParticipant' where DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.PhdProgramGuiding';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant' where DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.InternalGuiding';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.ExternalPhdParticipant' where DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.phd.ExternalGuiding';


update PHD_PARTICIPANT set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.phd.InternalGuiding';
update PHD_PARTICIPANT set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.phd.ExternalPhdParticipant' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.phd.ExternalGuiding';


alter table PHD_PARTICIPANT change column OID_ASSISTANT_PHD_INDIVIDUAL_PROGRAM_PROCESS OID_PROCESS_FOR_ASSISTANT_GUIDING bigint(20) null;
alter table PHD_PARTICIPANT change column OID_PHD_INDIVIDUAL_PROGRAM_PROCESS OID_PROCESS_FOR_GUIDING bigint(20) null;
alter table PHD_PARTICIPANT add column OID_INDIVIDUAL_PROCESS bigint(20) null;
alter table `PHD_PARTICIPANT` add index (`OID_INDIVIDUAL_PROCESS`);

update PHD_PARTICIPANT set OID_INDIVIDUAL_PROCESS = OID_PROCESS_FOR_ASSISTANT_GUIDING where OID_PROCESS_FOR_ASSISTANT_GUIDING IS NOT NULL;
update PHD_PARTICIPANT set OID_INDIVIDUAL_PROCESS = OID_PROCESS_FOR_GUIDING where OID_PROCESS_FOR_GUIDING IS NOT NULL;


alter table PHD_PARTICIPANT change column OID_INDIVIDUAL_PROCESS OID_INDIVIDUAL_PROCESS bigint(20) not null;

alter table PHD_PARTICIPANT add column WHEN_CREATED datetime null;

update PHD_PARTICIPANT set WHEN_CREATED = NOW();

alter table PHD_PARTICIPANT change column WHEN_CREATED WHEN_CREATED datetime not null;

alter table PHD_PARTICIPANT add column ACCESS_HASH_CODE varchar(255) null;
alter table PHD_PARTICIPANT add column ACCESS_TYPES text null;
alter table PHD_PARTICIPANT add column PASSWORD varchar(255) null;

drop table THESIS_JURY_ELEMENT;

create table `THESIS_JURY_ELEMENT` (
  `CREATION_DATE` datetime not null,
  `ELEMENT_ORDER` int(11) not null,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20) not null,
  `OID_PARTICIPANT` bigint(20) not null,
  `OID_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `REPORTER` tinyint(1) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PARTICIPANT),
  index (OID_PROCESS),
  index (OID_ROOT_DOMAIN_OBJECT),
  unique (OID_PROCESS,ELEMENT_ORDER)
) type=InnoDB, character set latin1 ;


