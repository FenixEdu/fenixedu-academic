alter table STUDENT_CURRICULAR_PLAN add SPECIALIZATION int(11);
alter table DEGREE_CURRICULAR_PLAN add KEY_DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO int(11);
alter table CURRICULAR_COURSE add EXECUTION_SCOPE int(11);
alter table CURRICULAR_COURSE add MANDATORY bit;
alter table CURRICULAR_COURSE add KEY_CURRICULAR_COURSE_ENROLMENT_INFO int(11);

CREATE TABLE almeida_disc (
  codInt int(11) NOT NULL auto_increment,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  anoDis int(11) default NULL,
  semDis int(11) default NULL,
  codDis varchar(200) default NULL,
  tipo int(11) default NULL,
  teo double(11,2) default NULL,
  pra double(11,2) default NULL,
  lab double(11,2) default NULL,
  teoPra double(11,2) default NULL,
  nomeDis varchar(200) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=MyISAM;

create table STUDENT_GROUP_INFO (
   ID_INTERNAL int(11) not null auto_increment,
   STUDENT_TYPE int(11) not null,
   MIN_COURSES_TO_ENROL int(11) not null,
   MAX_COURSES_TO_ENROL int(11) not null,
   MAX_NAC_TO_ENROL int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (STUDENT_TYPE)
)type=InnoDB;

CREATE TABLE almeida_inscricoes (
  codInt int(11) NOT NULL auto_increment,
  numero int(11) default NULL,
  ano int(11) default NULL,
  semestre int(11) default NULL,
  codDis varchar(200) default NULL,
  anoInscricao int(11) default NULL,
  curso int(11) default NULL,
  ramo varchar(200) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=MyISAM;