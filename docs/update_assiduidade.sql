-- 
--  Table structure for table 'ass_FUNCIONARIO'
-- 

-- DROP TABLE IF EXISTS ass_FUNCIONARIO;
-- CREATE TABLE ass_FUNCIONARIO (
  -- codigoInterno int(11) NOT NULL auto_increment,
  -- chavePessoa int(11) NOT NULL default '0',
  -- numeroMecanografico int(11) NOT NULL default '0',
  -- chaveHorarioActual int(11) NOT NULL default '0',
  -- antiguidade date NOT NULL default '0000-00-00',
  -- chaveFuncResponsavel int(11) NOT NULL default '0',
  -- chaveCCLocalTrabalho int(11) NOT NULL default '0',
  -- chaveCCCorrespondencia int(11) NOT NULL default '0',
  -- chaveCCVencimento int(11) NOT NULL default '0',
  -- calendario varchar(50) NOT NULL default '',
  -- chaveStatus int(11) NOT NULL default '0',
  -- dataInicio date NOT NULL default '0000-00-00',
  -- dataFim date default NULL,
  -- quem int(11) NOT NULL default '0',
  -- quando datetime NOT NULL default '0000-00-00 00:00:00',
  -- PRIMARY KEY  (codigoInterno),
  -- UNIQUE KEY u1 (numeroMecanografico),
  -- UNIQUE KEY u2 (chavePessoa)
-- ) TYPE=InnoDB;


--  Drop column of ass_FUNCIONARIO
ALTER TABLE ass_FUNCIONARIO DROP chaveFuncResponsavel;
ALTER TABLE ass_FUNCIONARIO DROP chaveCCLocalTrabalho;
ALTER TABLE ass_FUNCIONARIO DROP chaveCCCorrespondencia;
ALTER TABLE ass_FUNCIONARIO DROP chaveCCVencimento;
ALTER TABLE ass_FUNCIONARIO DROP calendario;
ALTER TABLE ass_FUNCIONARIO DROP chaveStatus;
ALTER TABLE ass_FUNCIONARIO DROP dataInicio;
ALTER TABLE ass_FUNCIONARIO DROP dataFim;
ALTER TABLE ass_FUNCIONARIO DROP quem;
ALTER TABLE ass_FUNCIONARIO DROP quando;

-- 
--  Table structure for table 'ass_FUNCIONARIO_HISTORICO'
-- 

DROP TABLE IF EXISTS ass_FUNCIONARIO_HISTORICO;
CREATE TABLE ass_FUNCIONARIO_HISTORICO (
  codigoInterno int(11) NOT NULL default '0' auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  chaveFuncResponsavel int(11) NULL default '0',
  chaveCCLocalTrabalho int(11) NULL default '0',
  chaveCCCorrespondencia int(11) NULL default '0',
  chaveCCVencimento int(11) NULL default '0',
  calendario varchar(50) NULL default '',
  chaveStatus int(11) NULL default '0',
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;

--Add quem e quando à tabela
ALTER TABLE ass_STATUS ADD quem int(11) NOT NULL default '0';
ALTER TABLE ass_STATUS ADD quando datetime NOT NULL default '0000-00-00 00:00:00';


alter table ass_HORARIO_TIPO add trabalhoConsecutivo time;
alter table ass_HORARIO add trabalhoConsecutivo time after posicao;
alter table ass_HORARIO_EXCEPCAO add trabalhoConsecutivo time after posicao;


update ass_HORARIO_TIPO set trabalhoConsecutivo='05:00:00' where modalidade<>'especifico' 
and modalidade<>'jornadaContinua' and modalidade<>'turnos' and modalidade<>'meioTempo';

update ass_HORARIO_TIPO set trabalhoConsecutivo='04:00:00' where modalidade='especifico' 
and duracaoSemanal=30 and inicioHN2 is not null;

update ass_HORARIO_TIPO set trabalhoConsecutivo='03:00:00' where modalidade='especifico' 
and duracaoSemanal=25 and inicioHN2 is not null;