--  MySQL-Front Dump 2.5
-- 
--  Host: localhost   Database: ciapl
--  --------------------------------------------------------
--  Server version 3.23.52-max-nt


-- 
--  Table structure for table 'ALMEIDA_CURRICULAR_COURSE'
-- 

DROP TABLE IF EXISTS ALMEIDA_CURRICULAR_COURSE;
CREATE TABLE ALMEIDA_CURRICULAR_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CODE varchar(11) NOT NULL default '',
  NAME varchar(255) NOT NULL default '',
  UNIVERSITY_CODE varchar(255) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  KEY U1 (CODE)
) TYPE=MyISAM;



-- 
--  Table structure for table 'ALMEIDA_ENROLMENT'
-- 

DROP TABLE IF EXISTS ALMEIDA_ENROLMENT;
CREATE TABLE ALMEIDA_ENROLMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NUMALU varchar(11) NOT NULL default '',
  ANOINS int(11) NOT NULL default '0',
  ANODIS int(11) NOT NULL default '0',
  SEMDIS int(11) NOT NULL default '0',
  EPOCA int(11) NOT NULL default '0',
  CODDIS varchar(11) NOT NULL default '',
  CURSO varchar(11) NOT NULL default '',
  RAMO int(11) NOT NULL default '0',
  RESULT varchar(11) default NULL,
  NUMDOC varchar(11) default NULL,
  DATEXA varchar(11) default NULL,
  CODUNIV varchar(11) default NULL,
  OBSERV varchar(255) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;



-- 
--  Table structure for table 'ALMEIDA_ESCOLA'
-- 

DROP TABLE IF EXISTS ALMEIDA_ESCOLA;
CREATE TABLE ALMEIDA_ESCOLA (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CODE varchar(10) NOT NULL default '',
  NAME varchar(150) NOT NULL default '',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;



-- 
--  Table structure for table 'almeida_curram'
-- 

DROP TABLE IF EXISTS ALMEIDA_CURRAM;
CREATE TABLE ALMEIDA_CURRAM (
  codInt int(11) NOT NULL auto_increment,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  codOrien int(11) default NULL,
  descri varchar(200) default NULL,
  PRIMARY KEY  (codInt),
  KEY U1 (codCur,codRam,codOrien)
) TYPE=MyISAM;



-- 
--  Table structure for table 'almeida_disc'
-- 

DROP TABLE IF EXISTS ALMEIDA_DISC;
CREATE TABLE ALMEIDA_DISC (
  codInt int(11) NOT NULL auto_increment,
  anoLectivo int(11) default NULL,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  codOrien int(11) default NULL,
  creditos int(11) default NULL,
  anoDis int(11) default NULL,
  semDis int(11) default NULL,
  codDis varchar(200) default NULL,
  tipo int(11) default NULL,
  teo double(11,2) default NULL,
  pra double(11,2) default NULL,
  lab double(11,2) default NULL,
  teoPra double(11,2) default NULL,
  nomeDis varchar(200) default NULL,
  codUniversidade varchar(200) default NULL,
  PRIMARY KEY  (codInt),
  KEY U1 (codCur,codDis,codRam,semDis,anoLectivo,anoDis,creditos)
) TYPE=MyISAM;

