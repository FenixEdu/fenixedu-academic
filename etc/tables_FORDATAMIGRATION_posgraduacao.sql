# MySQL-Front Dump 2.5
#
# Host: localhost   Database: ciapl
# --------------------------------------------------------
# Server version 3.23.52-max-nt


#
# Table structure for table 'posgrad_aluno_mestrado'
#

DROP TABLE IF EXISTS posgrad_aluno_mestrado;
CREATE TABLE posgrad_aluno_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  numero int(10) NOT NULL default '0',
  codigoPessoa int(11) NOT NULL default '0',
  codigoCursoMestrado int(11) NOT NULL default '0',
  especializacao enum('Mestrado','Integrado','Especialização') NOT NULL default 'Mestrado',
  anoCandidatura int(11) NOT NULL default '0',
  escolaLicenciatura varchar(100) default NULL,
  anoLicenciatura int(11) default '0',
  media float default '0',
  creditos varchar(4) default NULL,
  observacoes varchar(255) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u2 (codigoPessoa,codigoCursoMestrado),
  UNIQUE KEY u1 (numero)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_area_cientifica'
#

DROP TABLE IF EXISTS posgrad_area_cientifica;
CREATE TABLE posgrad_area_cientifica (
  codigoInterno int(11) NOT NULL auto_increment,
  nome varchar(100) NOT NULL default '',
  codigoCursoMestrado int(11) NOT NULL default '0',
  anoLectivo varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (nome,codigoCursoMestrado,anoLectivo)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_candidato_mestrado'
#

DROP TABLE IF EXISTS posgrad_candidato_mestrado;
CREATE TABLE posgrad_candidato_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroDocumentoIdentificacao varchar(50) default NULL,
  tipoDocumentoIdentificacao enum('BILHETE DE IDENTIDADE','PASSAPORTE','OUTRO') default 'BILHETE DE IDENTIDADE',
  localEmissaoDocumentoIdentificacao varchar(100) default NULL,
  dataEmissaoDocumentoIdentificacao date default '0001-01-01',
  nome varchar(100) default NULL,
  sexo enum('masculino','feminino') default 'masculino',
  estadoCivil enum('SOLTEIRO','CASADO','DIVORCIADO','VIÚVO','SEPARADO','UNIÃO DE FACTO') default 'SOLTEIRO',
  nascimento date default '0001-01-01',
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
  email varchar(50) default NULL,
  enderecoWeb varchar(200) default NULL,
  numContribuinte varchar(50) default NULL,
  profissao varchar(100) default NULL,
  licenciatura varchar(100) default NULL,
  username varchar(50) default NULL,
  password varchar(50) default NULL,
  codigoCursoMestrado int(11) default NULL,
  numeroCandidato int(20) default NULL,
  anoCandidatura int(20) default NULL,
  especializacao enum('Mestrado','Integrado','Especialização') default 'Mestrado',
  escolaLicenciatura varchar(100) default NULL,
  anoLicenciatura int(11) default '0',
  media float default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u3 (username),
  UNIQUE KEY u2 (numeroCandidato,codigoCursoMestrado,anoCandidatura),
  UNIQUE KEY u1 (numeroDocumentoIdentificacao,tipoDocumentoIdentificacao,codigoCursoMestrado)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_candidato_situacao'
#

DROP TABLE IF EXISTS posgrad_candidato_situacao;
CREATE TABLE posgrad_candidato_situacao (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoCandidato int(11) NOT NULL default '0',
  codigoSituacao int(11) NOT NULL default '1',
  data date NOT NULL default '2002-10-18',
  observacoes text,
  valido tinyint(4) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_cargo'
#

DROP TABLE IF EXISTS posgrad_cargo;
CREATE TABLE posgrad_cargo (
  codigoInterno int(11) NOT NULL auto_increment,
  cargoNome enum('POS_GRADUACAO','CANDIDATO_AUXILIAR','CANDIDATO','ALUNO_MESTRADO') NOT NULL default 'POS_GRADUACAO',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_curso_mestrado'
#

DROP TABLE IF EXISTS posgrad_curso_mestrado;
CREATE TABLE posgrad_curso_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  nomeMestrado varchar(100) NOT NULL default '',
  anoLectivo varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u2 (nomeMestrado)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_disc_area'
#

DROP TABLE IF EXISTS posgrad_disc_area;
CREATE TABLE posgrad_disc_area (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoAreaCientifica int(11) NOT NULL default '0',
  codigoDisciplina int(11) NOT NULL default '0',
  anoLectivo varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_disc_area_aluno'
#

DROP TABLE IF EXISTS posgrad_disc_area_aluno;
CREATE TABLE posgrad_disc_area_aluno (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoAluno int(11) NOT NULL default '0',
  codigoAreaDisciplina int(11) NOT NULL default '0',
  dataExame date default NULL,
  dataLancamento date default NULL,
  nota varchar(4) default NULL,
  equivalencia varchar(15) default NULL,
  creditos varchar(4) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (codigoAluno,codigoAreaDisciplina)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_disciplina'
#

DROP TABLE IF EXISTS posgrad_disciplina;
CREATE TABLE posgrad_disciplina (
  codigoInterno int(11) NOT NULL auto_increment,
  nome varchar(200) NOT NULL default '',
  codigoCursoMestrado int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (nome,codigoCursoMestrado)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_docente'
#

DROP TABLE IF EXISTS posgrad_docente;
CREATE TABLE posgrad_docente (
  codigoInterno int(11) NOT NULL auto_increment,
  cat varchar(4) default NULL,
  numdec int(5) NOT NULL default '0',
  bi varchar(12) default NULL,
  cont varchar(15) default NULL,
  nome varchar(100) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM;



#
# Table structure for table 'posgrad_guia'
#

DROP TABLE IF EXISTS posgrad_guia;
CREATE TABLE posgrad_guia (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroGuia int(50) NOT NULL default '0',
  dataEmissao date NOT NULL default '0001-01-01',
  anoGuia int(11) NOT NULL default '0',
  situacao enum('PAGA','NAO_PAGA','ANULADA') NOT NULL default 'PAGA',
  entidadeContribuinte int(11) NOT NULL default '0',
  entidadeNome varchar(100) NOT NULL default '',
  entidadeMorada varchar(100) NOT NULL default '',
  numeroAluno int(11) NOT NULL default '0',
  tipoAluno enum('CANDIDATO','MESTRADO','ESPECIALIZAÇÃO','DOUTORAMENTO') NOT NULL default 'CANDIDATO',
  codigoCursoMestrado int(11) NOT NULL default '0',
  total float(11,2) NOT NULL default '0.00',
  observacoes text,
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_guia_tabela'
#

DROP TABLE IF EXISTS posgrad_guia_tabela;
CREATE TABLE posgrad_guia_tabela (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoGuia int(11) NOT NULL default '0',
  codigoTabela int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 379904 kB; InnoDB free:';



#
# Table structure for table 'posgrad_pagamento_guia'
#

DROP TABLE IF EXISTS posgrad_pagamento_guia;
CREATE TABLE posgrad_pagamento_guia (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoGuia int(11) NOT NULL default '0',
  dataPagamento date NOT NULL default '0001-01-01',
  montante float(10,2) NOT NULL default '0.00',
  numeroPrestacao int(11) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_pessoa'
#

DROP TABLE IF EXISTS posgrad_pessoa;
CREATE TABLE posgrad_pessoa (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroDocumentoIdentificacao varchar(50) default NULL,
  tipoDocumentoIdentificacao enum('BILHETE DE IDENTIDADE','PASSAPORTE','OUTRO') default 'BILHETE DE IDENTIDADE',
  localEmissaoDocumentoIdentificacao varchar(100) default NULL,
  dataEmissaoDocumentoIdentificacao date default '2002-01-01',
  nome varchar(100) default NULL,
  sexo enum('masculino','feminino') default 'masculino',
  estadoCivil enum('SOLTEIRO','CASADO','DIVORCIADO','VIÚVO','SEPARADO','UNIÃO DE FACTO') default 'SOLTEIRO',
  nascimento date default '2002-01-01',
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
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (username),
  UNIQUE KEY u2 (numeroDocumentoIdentificacao,tipoDocumentoIdentificacao)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_pessoa_cargo'
#

DROP TABLE IF EXISTS posgrad_pessoa_cargo;
CREATE TABLE posgrad_pessoa_cargo (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoPessoa int(11) NOT NULL default '0',
  codigoCargo int(11) NOT NULL default '0',
  data date NOT NULL default '0001-01-01',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (codigoPessoa,codigoCargo)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';



#
# Table structure for table 'posgrad_situacao'
#

DROP TABLE IF EXISTS posgrad_situacao;
CREATE TABLE posgrad_situacao (
  codigoInterno int(11) NOT NULL auto_increment,
  situacao enum('PENDENTE','ADMITIDO','SUPLENTE','NAO_ACEITE','DESISTIU','FALTA_CERTIFICADO') NOT NULL default 'PENDENTE',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM;



#
# Table structure for table 'posgrad_tabela_precos'
#

DROP TABLE IF EXISTS posgrad_tabela_precos;
CREATE TABLE posgrad_tabela_precos (
  codigoInterno int(11) NOT NULL auto_increment,
  tipo varchar(200) NOT NULL default '',
  designacao varchar(200) NOT NULL default '',
  montante float(10,2) NOT NULL default '0.00',
  valido int(11) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

