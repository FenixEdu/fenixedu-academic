# ------------------------------------------
# Table structure for table 'almeida_aluno'
# ------------------------------------------
DROP TABLE IF EXISTS almeida_aluno;
CREATE TABLE almeida_aluno (
  numero int(11) NOT NULL default '0',
  nome varchar(200) default NULL,
  nascimento date default NULL,
  bi varchar(15) default NULL,
  curso int(11) default NULL,
  ramo int(11) default NULL,
  sexo char(1) default NULL,
  nacionalidade varchar(11) default NULL,
  freguesia varchar(50) default NULL,
  concelho varchar(50) default NULL,
  distrito varchar(50) default NULL,
  nomePai varchar(50) default NULL,
  nomeMae varchar(50) default NULL,
  morada varchar(50) default NULL,
  localidadeMorada varchar(50) default NULL,
  cp varchar(50) default NULL,
  localidadeCP varchar(50) default NULL,
  telefone varchar(50) default NULL,
  email varchar(50) default NULL,
  PRIMARY KEY  (numero)
) TYPE=MyISAM;



#
# Table structure for table 'almeida_curram'
#

DROP TABLE IF EXISTS almeida_curram;
CREATE TABLE almeida_curram (
  codInt int(11) NOT NULL auto_increment,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  codOrien int(11) default NULL,
  descri varchar(200) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=MyISAM;



#
# Dumping data for table 'almeida_curram'
#



#
# Table structure for table 'almeida_disc'
#

DROP TABLE IF EXISTS almeida_disc;
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



#
# Dumping data for table 'almeida_disc'
#



#
# Table structure for table 'almeida_inscricoes'
#

DROP TABLE IF EXISTS almeida_inscricoes;
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



#
# Dumping data for table 'almeida_inscricoes'
#



#
# Table structure for table 'almeida_nacionalidade'
#

DROP TABLE IF EXISTS almeida_nacionalidade;
CREATE TABLE almeida_nacionalidade (
  numero int(11) NOT NULL default '0',
  nome varchar(200) default NULL,
  PRIMARY KEY  (numero)
) TYPE=MyISAM;



#
# Dumping data for table 'almeida_nacionalidade'
#

