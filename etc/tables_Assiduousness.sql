-- MySQL dump 8.14
--
-- Host: localhost    Database: fenix
-- --------------------------------------------------------
-- Server version	3.23.39-max-nt

--
-- Table structure for table 'ass_CARGO'
--

DROP TABLE IF EXISTS ass_CARGO;
CREATE TABLE ass_CARGO (
  chaveCargo int(11) NOT NULL default '0',
  cargo enum('ALUNO','FUNCDOCENTE','FUNCNAODOCENTE','ADMINISTRATOR','GestaoAssiduidade','ConsultaAssiduidade') NOT NULL default 'ALUNO',
  PRIMARY KEY  (chaveCargo)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table 'ass_CARGO_URL'
--

DROP TABLE IF EXISTS ass_CARGO_URL;
CREATE TABLE ass_CARGO_URL (
  chaveCargo int(11) NOT NULL default '0',
  chaveUrl int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveCargo,chaveUrl)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_CARTAO'
--

DROP TABLE IF EXISTS ass_CARTAO;
CREATE TABLE ass_CARTAO (
  codigoInterno int(11) NOT NULL auto_increment,
  numCartao int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  dataInicio datetime NOT NULL default '0000-00-00 00:00:00',
  dataFim datetime default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  estado varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_CENTRO_CUSTO'
--

DROP TABLE IF EXISTS ass_CENTRO_CUSTO;
CREATE TABLE ass_CENTRO_CUSTO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(50) NOT NULL default '',
  departamento varchar(50) default NULL,
  seccao1 varchar(50) default NULL,
  seccao2 varchar(50) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY sigla (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_FERIADO'
--

DROP TABLE IF EXISTS ass_FERIADO;
CREATE TABLE ass_FERIADO (
  codigoInterno int(11) NOT NULL auto_increment,
  tipoFeriado varchar(15) NOT NULL default '',
  descricao varchar(50) default NULL,
  data date default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (tipoFeriado,data)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_FERIAS'
--

DROP TABLE IF EXISTS ass_FERIAS;
CREATE TABLE ass_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  anoCorrente int(11) NOT NULL default '0',
  diasNormais int(11) NOT NULL default '0',
  diasEpocaBaixa int(11) NOT NULL default '0',
  diasHorasExtras int(11) NOT NULL default '0',
  diasAntiguidade int(11) NOT NULL default '0',
  diasMeioDia int(11) NOT NULL default '0',
  diasDispensaServico int(11) NOT NULL default '0',
  diasTolerancia int(11) NOT NULL default '0',
  diasTransferidos int(11) NOT NULL default '0',
  diasTransHorasExtras int(11) NOT NULL default '0',
  diasTransAntiguidade int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_FUNCIONARIO'
--

DROP TABLE IF EXISTS ass_FUNCIONARIO;
CREATE TABLE ass_FUNCIONARIO (
  codigoInterno int(11) NOT NULL auto_increment,
  chavePessoa int(11) NOT NULL default '0',
  numeroMecanografico int(11) NOT NULL default '0',
  chaveHorarioActual int(11) NOT NULL default '0',
  antiguidade date NOT NULL default '0000-00-00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numeroMecanografico),
  UNIQUE KEY u2 (chavePessoa)
) TYPE=InnoDB;

--
-- Table structure for table 'ass_FUNCIONARIO_HISTORICO'
--

DROP TABLE IF EXISTS ass_FUNCIONARIO_HISTORICO;
CREATE TABLE ass_FUNCIONARIO_HISTORICO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  chaveFuncResponsavel int(11) default '0',
  chaveCCLocalTrabalho int(11) default '0',
  chaveCCCorrespondencia int(11) default '0',
  chaveCCVencimento int(11) default '0',
  calendario varchar(50) default '',
  chaveStatus int(11) default '0',
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;


--
-- Table structure for table 'ass_FUNC_NAO_DOCENTE'
--

DROP TABLE IF EXISTS ass_FUNC_NAO_DOCENTE;
CREATE TABLE ass_FUNC_NAO_DOCENTE (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (chaveFuncionario)
) TYPE=InnoDB;


--
-- Table structure for table 'ass_HORARIO'
--

DROP TABLE IF EXISTS ass_HORARIO;
CREATE TABLE ass_HORARIO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  sigla varchar(50) default NULL,
  modalidade varchar(50) default NULL,
  duracaoSemanal float default NULL,
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime default NULL,
  fimHN1 datetime default NULL,
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDias int(10) default NULL,
  posicao int(10) default NULL,
  trabalhoConsecutivo time default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';


--
-- Table structure for table 'ass_HORARIOEXCEPCAO_REGIME'
--

DROP TABLE IF EXISTS ass_HORARIOEXCEPCAO_REGIME;
CREATE TABLE ass_HORARIOEXCEPCAO_REGIME (
  chaveHorario int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorario,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';



--
-- Table structure for table 'ass_HORARIOTIPO_REGIME'
--

DROP TABLE IF EXISTS ass_HORARIOTIPO_REGIME;
CREATE TABLE ass_HORARIOTIPO_REGIME (
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorarioTipo,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_HORARIO_EXCEPCAO'
--

DROP TABLE IF EXISTS ass_HORARIO_EXCEPCAO;
CREATE TABLE ass_HORARIO_EXCEPCAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  sigla varchar(50) default NULL,
  modalidade varchar(50) default NULL,
  duracaoSemanal float default NULL,
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime default NULL,
  fimHN1 datetime default NULL,
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDias int(10) default NULL,
  posicao int(10) default NULL,
  trabalhoConsecutivo time default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';


--
-- Table structure for table 'ass_HORARIO_REGIME'
--

DROP TABLE IF EXISTS ass_HORARIO_REGIME;
CREATE TABLE ass_HORARIO_REGIME (
  chaveHorario int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorario,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_HORARIO_TIPO'
--

DROP TABLE IF EXISTS ass_HORARIO_TIPO;
CREATE TABLE ass_HORARIO_TIPO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(50) NOT NULL default '',
  modalidade varchar(50) NOT NULL default '',
  duracaoSemanal float NOT NULL default '0',
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime NOT NULL default '0000-00-00 00:00:00',
  fimHN1 datetime NOT NULL default '0000-00-00 00:00:00',
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  trabalhoConsecutivo time default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';


--
-- Table structure for table 'ass_JUSTIFICACAO'
--

DROP TABLE IF EXISTS ass_JUSTIFICACAO;
CREATE TABLE ass_JUSTIFICACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveParamJustificacao int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  diaInicio date NOT NULL default '0000-00-00',
  horaInicio time default NULL,
  diaFim date default NULL,
  horaFim time default NULL,
  observacao varchar(50) default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  KEY indexFuncionario (chaveFuncionario),
  KEY indexDatas (diaInicio,diaFim)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_MARCACAO_PONTO'
--

DROP TABLE IF EXISTS ass_MARCACAO_PONTO;
CREATE TABLE ass_MARCACAO_PONTO (
  codigoInterno int(11) NOT NULL auto_increment,
  unidade int(11) default NULL,
  siglaUnidade varchar(50) default NULL,
  dataMarcacao datetime default NULL,
  numCartao int(11) default NULL,
  numFuncionario int(11) default NULL,
  estado varchar(50) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';



--
-- Table structure for table 'ass_MODALIDADE'
--

DROP TABLE IF EXISTS ass_MODALIDADE;
CREATE TABLE ass_MODALIDADE (
  codigoInterno int(11) NOT NULL auto_increment,
  designacao varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_PARAM_FERIAS'
--

DROP TABLE IF EXISTS ass_PARAM_FERIAS;
CREATE TABLE ass_PARAM_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(10) NOT NULL default '',
  designacao varchar(75) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_PARAM_JUSTIFICACAO'
--

DROP TABLE IF EXISTS ass_PARAM_JUSTIFICACAO;
CREATE TABLE ass_PARAM_JUSTIFICACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(15) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  tipo varchar(4) NOT NULL default '',
  tipoDias char(1) NOT NULL default '',
  grupo varchar(10) default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';


--
-- Table structure for table 'ass_PARAM_REGULARIZACAO'
--

DROP TABLE IF EXISTS ass_PARAM_REGULARIZACAO;
CREATE TABLE ass_PARAM_REGULARIZACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(7) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_PERIODO_FERIAS'
--

DROP TABLE IF EXISTS ass_PERIODO_FERIAS;
CREATE TABLE ass_PERIODO_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDiasUteis int(11) NOT NULL default '0',
  tipoFerias int(11) NOT NULL default '1',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_PESSOA_CARGO'
--

DROP TABLE IF EXISTS ass_PESSOA_CARGO;
CREATE TABLE ass_PESSOA_CARGO (
  chavePessoa int(11) NOT NULL default '0',
  chaveCargo int(11) NOT NULL default '0',
  PRIMARY KEY  (chavePessoa,chaveCargo)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_REGIME'
--

DROP TABLE IF EXISTS ass_REGIME;
CREATE TABLE ass_REGIME (
  codigoInterno int(11) NOT NULL auto_increment,
  designacao varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_REGULARIZACAO_MARCACAO'
--

DROP TABLE IF EXISTS ass_REGULARIZACAO_MARCACAO;
CREATE TABLE ass_REGULARIZACAO_MARCACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveMarcacaoPonto int(11) NOT NULL default '0',
  chaveParamRegularizacao varchar(50) NOT NULL default '',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (chaveMarcacaoPonto)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_STATUS'
--

DROP TABLE IF EXISTS ass_STATUS;
CREATE TABLE ass_STATUS (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(10) NOT NULL default '',
  designacao varchar(50) NOT NULL default '',
  estado enum('activo','inactivo','pendente') NOT NULL default 'activo',
  assiduidade enum('true','false') NOT NULL default 'true',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';


--
-- Table structure for table 'ass_UNIDADE_MARCACAO'
--

DROP TABLE IF EXISTS ass_UNIDADE_MARCACAO;
CREATE TABLE ass_UNIDADE_MARCACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(20) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  id int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table 'ass_URL'
--

DROP TABLE IF EXISTS ass_URL;
CREATE TABLE ass_URL (
  chaveUrl int(11) NOT NULL default '0',
  url varchar(50) NOT NULL default '',
  PRIMARY KEY  (chaveUrl)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';
