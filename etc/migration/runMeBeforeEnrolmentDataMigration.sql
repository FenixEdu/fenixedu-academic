drop table if exists ALMEIDA_CURRICULAR_COURSE;
create table ALMEIDA_CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   CODE int(11) not null,
   NAME varchar(255) not null,
   primary key (ID_INTERNAL)
) type=InnoDB;

drop table if exists ALMEIDA_ENROLMENT;
create table ALMEIDA_ENROLMENT (
   ID_INTERNAL int(11) not null auto_increment,
   NUMALU varchar(11) not null,
   ANOINS int(11) not null,
   ANODIS int(11) not null,
   SEMDIS int(11) not null,
   EPOCA int(11) not null,
   CODDIS varchar(11) not null,
   CURSO varchar(11) not null,
   RAMO int(11) not null,
   RESULT varchar(11),
   NUMDOC varchar(11),
   DATEXA varchar(11),
   CODUNIV varchar(11),
   OBSERV varchar(255),
   primary key (ID_INTERNAL)
) type=InnoDB;
