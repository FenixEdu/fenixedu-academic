# MySQL-Front Dump 2.5
#
# Host: localhost   Database: bolseiros
# --------------------------------------------------------
# Server version 4.0.17-nt

# This file defines the MW tables for the Grant Application data migration


#
# Table structure for table 'mwgrant_bolseiro'
#
DROP TABLE IF EXISTS mwgrant_bolseiro;
CREATE TABLE mwgrant_bolseiro (
  codigoInterno int(5) NOT NULL auto_increment,
  numero int(5) default NULL,
  dataEnvioCGD date default NULL,
  numeroViaCartao int(255) default NULL,
  chavePessoa int(5) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (chavePessoa)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_categorias'
#
DROP TABLE IF EXISTS mwgrant_categorias;
CREATE TABLE mwgrant_categorias (
  codigoInterno int(10) NOT NULL default '0',
  codigoCategoria varchar(255) default NULL,
  categoriaAbreviada varchar(255) default NULL,
  categoriaLonga varchar(255) default NULL,
  UNIQUE KEY PrimaryKey (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_centrocusto'
#
DROP TABLE IF EXISTS mwgrant_centrocusto;
CREATE TABLE mwgrant_centrocusto (
  codigoInterno int(11) NOT NULL auto_increment,
  numero int(11) NOT NULL default '0',
  designacao varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numero)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_comparticipacao'
#
DROP TABLE IF EXISTS mwgrant_comparticipacao;
CREATE TABLE mwgrant_comparticipacao (
  codigoInterno int(15) NOT NULL auto_increment,
  percentagem int(11) default '100',
  chaveSubsidio int(11) NOT NULL default '0',
  chaveEntidadePagadora int(11) NOT NULL default '0',
  chaveResponsavel int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (chaveSubsidio,chaveEntidadePagadora)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_contrato'
#
DROP TABLE IF EXISTS mwgrant_contrato;
CREATE TABLE mwgrant_contrato (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroContrato int(11) NOT NULL default '0',
  dataInicioContrato date default NULL,
  dataFimContrato date default NULL,
  motivoFimContrato varchar(255) default NULL,
  chaveBolseiro int(11) NOT NULL default '0',
  chaveTipoBolsa int(11) NOT NULL default '0',
  chaveConcurso int(11) default NULL,
  chaveEntidadePagadora int(11) NOT NULL default '0',
  chaveRegimeActual int(11) NOT NULL default '0',
  chaveSubsidioActual int(11) NOT NULL default '0',
  chaveOrientadorActual int(11) NOT NULL default '0',
  chaveResponsavelActual int(11) NOT NULL default '0',
  chaveLocalTrabalhoActual int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (numeroContrato,chaveBolseiro)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_cursos'
#
DROP TABLE IF EXISTS mwgrant_cursos;
CREATE TABLE mwgrant_cursos (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoCurso int(11) NOT NULL default '0',
  sigla varchar(4) default NULL,
  nomeCompleto varchar(80) default NULL,
  nomeAbreviado varchar(80) default NULL,
  area varchar(50) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (codigoCurso)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_docente'
#
DROP TABLE IF EXISTS mwgrant_docente;
CREATE TABLE mwgrant_docente (
  codigoInterno int(10) NOT NULL auto_increment,
  numero int(10) default NULL,
  chaveDepartamento int(10) default NULL,
  grau varchar(255) default NULL,
  categoria varchar(255) default NULL,
  chavePessoa int(10) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numero)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_entidadepagadora'
#
DROP TABLE IF EXISTS mwgrant_entidadepagadora;
CREATE TABLE mwgrant_entidadepagadora (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveCentroCusto varchar(50) default NULL,
  chaveProjecto varchar(50) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_habilitacao'
#
DROP TABLE IF EXISTS mwgrant_habilitacao;
CREATE TABLE mwgrant_habilitacao (
  codigoInterno int(11) NOT NULL auto_increment,
  dataObtencao date default NULL,
  classificacao varchar(255) default NULL,
  dominioRamo varchar(255) default NULL,
  areaEspecializacao varchar(255) default NULL,
  estabelecimentoObtencao varchar(255) default NULL,
  reconhecimentoGrau varchar(255) default NULL,
  dataEquivalencia date default NULL,
  estabelecimentoEquivalencia varchar(255) default NULL,
  chaveCursos int(11) default NULL,
  chaveGrauAcademicos int(11) default NULL,
  chavePaises int(11) default NULL,
  chavePessoa int(5) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (chavePessoa)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_orientacao'
#
DROP TABLE IF EXISTS mwgrant_orientacao;
CREATE TABLE mwgrant_orientacao (
  codigoInterno int(11) NOT NULL auto_increment,
  dataInicio date default NULL,
  dataFim date default NULL,
  chaveDocente int(11) NOT NULL default '0',
  chaveContrato int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_pessoa'
#
DROP TABLE IF EXISTS mwgrant_pessoa;
CREATE TABLE mwgrant_pessoa (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroDocumentoIdentificacao varchar(50) default NULL,
  tipoDocumentoIdentificacao int(11) NOT NULL default '0',
  localEmissaoDocumentoIdentificacao varchar(100) default NULL,
  dataEmissaoDocumentoIdentificacao date default NULL,
  dataValidadeDocumentoIdentificacao date default NULL,
  nome varchar(100) default NULL,
  sexo int(11) default NULL,
  estadoCivil int(11) default NULL,
  nascimento date default NULL,
  nomePai varchar(100) default NULL,
  nomeMae varchar(100) default NULL,
  nacionalidade varchar(50) default NULL,
  freguesiaNaturalidade varchar(100) default NULL,
  concelhoNaturalidade varchar(100) default NULL,
  distritoNaturalidade varchar(100) default NULL,
  morada varchar(100) default NULL,
  localidade varchar(100) default NULL,
  codigoPostal varchar(8) default NULL,
  freguesiaMorada varchar(100) default NULL,
  concelhoMorada varchar(100) default NULL,
  distritoMorada varchar(100) default NULL,
  telefone varchar(50) default NULL,
  telemovel varchar(50) default NULL,
  email varchar(100) default NULL,
  enderecoWeb varchar(200) default NULL,
  numContribuinte varchar(50) default NULL,
  profissao varchar(100) default NULL,
  username varchar(50) default NULL,
  password varchar(50) default NULL,
  nacionalidadeCompleta varchar(10) default NULL,
  codigoFiscal varchar(10) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_projecto'
#
DROP TABLE IF EXISTS mwgrant_projecto;
CREATE TABLE mwgrant_projecto (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroProjecto varchar(255) default NULL,
  designacaoProjecto varchar(255) default NULL,
  chaveDocente int(10) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_subsidio'
#
DROP TABLE IF EXISTS mwgrant_subsidio;
CREATE TABLE mwgrant_subsidio (
  codigoInterno int(11) NOT NULL auto_increment,
  extenso varchar(255) default NULL,
  valor double(11,2) default NULL,
  encargoTotal double(11,2) default NULL,
  dataInicio date default NULL,
  cabimento enum('SIM') default NULL,
  chaveContrato int(11) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


#
# Table structure for table 'mwgrant_tipobolsa'
#
DROP TABLE IF EXISTS mwgrant_tipobolsa;
CREATE TABLE mwgrant_tipobolsa (
  codigoInterno int(11) NOT NULL auto_increment,
  nome varchar(50) NOT NULL default 'MESTRADO',
  sigla varchar(50) NOT NULL default '',
  periodoMinimo int(2) NOT NULL default '0',
  periodoMaximo int(2) NOT NULL default '0',
  valorIndicativo double(10,2) NOT NULL default '0.00',
  origem varchar(10) NOT NULL default 'BIST',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB;

