-- ----------------------------
--  Table structure for ENROLMENT_EQUIVALENCE
-- ----------------------------
drop table if exists ENROLMENT_EQUIVALENCE;
create table ENROLMENT_EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_ENROLMENT int(11) not null,
   unique U1 (KEY_ENROLMENT),
   primary key (ID_INTERNAL)
)type=InnoDB;

-- ----------------------------
--  Table structure for EQUIVALENT_ENROLMENT_FOR_ENROLMENT_EQUIVALENCE
-- ----------------------------
drop table if exists EQUIVALENT_ENROLMENT_FOR_ENROLMENT_EQUIVALENCE;
create table EQUIVALENT_ENROLMENT_FOR_ENROLMENT_EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_ENROLMENT_EQUIVALENCE int(11) not null,
   KEY_EQUIVALENT_ENROLMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_ENROLMENT_EQUIVALENCE, KEY_EQUIVALENT_ENROLMENT)
)type=InnoDB;
