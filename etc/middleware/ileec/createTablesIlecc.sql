drop table if exists mw_AREAS_CIENTIFICAS_ILEEC;
create table mw_AREAS_CIENTIFICAS_ILEEC(
  id_area_cientifica int(11) not null,
  nome varchar(255) not null,
  primary key (id_area_cientifica)
)type = InnoDB;

drop table if exists mw_AREAS_ESPECIALIZACAO_ILEEC;
create table mw_AREAS_ESPECIALIZACAO_ILEEC(
  id_area_especializacao int(11) not null,
  nome varchar(255) not null,
  max_creditos int(11) not null,
  primary key (id_area_especializacao)
)type = InnoDB;

drop table if exists mw_AREAS_SECUNDARIAS_ILEEC;
create table mw_AREAS_SECUNDARIAS_ILEEC(
  id_area_secundaria int(11) not null,
  id_area_especializacao int(11) not null,
  nome varchar(255) not null,
  max_creditos int(11) not null,
  primary key (id_area_secundaria)
)type = InnoDB;

drop table if exists mw_DISCIPLINA_GRUPO_ILEEC;
create table mw_DISCIPLINA_GRUPO_ILEEC(
  id_disciplina int(11) not null,
  id_grupo int(11) not null,
  primary key (id_disciplina,id_grupo)
)type = InnoDB;

drop table if exists mw_DISCIPLINAS_ILEEC;
create table mw_DISCIPLINAS_ILEEC(
  id_disciplina int(11) not null,
  codigo_disciplina varchar(255) not null,
  id_ano_curricular int(11) not null,
  nome varchar(255) not null,
  numero_creditos int(11) not null,
  id_area_cientifica int(11) not null,
  obrigatoria int(11) not null,
  id_semestre int(11) not null,
  tipo_precedencia int(11) not null,
  insc_obrigatoria int(11) not null,
  funciona int(11) not null,
  primary key (id_disciplina)
)type = InnoDB;

drop table if exists mw_EQUIVALENCIAS_ILEEC;
create table mw_EQUIVALENCIAS_ILEEC(
  id_equivalencia int(11) not null,
  codigo_disciplina_curriculo_antigo varchar(255) not null,
  codigo_disciplina_curriculo_actual varchar(255) not null,
  tipo_equivalencia int(11) not null,
  id_area_cientifica int(11) not null,
  id_area_secundaria int(11) not null,
  id_area_especializacao int(11) not null,
  primary key (id_equivalencia)
)type = InnoDB;

drop table if exists mw_GRUPOS_ILEEC;
create table mw_GRUPOS_ILEEC(
  id_grupo int(11) not null,
  id_area_especializacao int(11) not null,
  id_area_secundaria int(11) not null,
  max_creditos int(11) not null,
  min_creditos int(11) not null,
  primary key (id_grupo)
)type = InnoDB;

drop table if exists mw_PRECEDENCIAS_DISCIPLINA_DISCIPLINA_ILEEC;
create table mw_PRECEDENCIAS_DISCIPLINA_DISCIPLINA_ILEEC(
  id_disciplina int(11) not null,
  id_precedente int(11) not null,
  primary key (id_disciplina,id_precedente)
)type = InnoDB;

drop table if exists mw_PRECEDENCIAS_NUMERO_DISCIPLINAS_ILEEC;
create table mw_PRECEDENCIAS_NUMERO_DISCIPLINAS_ILEEC(
  id_disciplina int(11) not null,
  numero_disciplinas int(11) not null,
  primary key (id_disciplina,numero_disciplinas)
)type=InnoDB;

drop table if exists mw_TIPOS_EQUIVALENCIA_ILEEC;
create table mw_TIPOS_EQUIVALENCIA_ILEEC(
  tipo_equivalencia int(11) not null,
  descricao varchar(255) not null,
  primary key (tipo_equivalencia)
)type = InnoDB;

drop table if exists mw_TIPOS_PRECENDENCIA_ILEEC;
create table mw_TIPOS_PRECENDENCIA_ILEEC(
  tipo_precedencia int(11) not null,
  descricao varchar(255) not null,
  primary key (tipo_precedencia)
)type = InnoDB;

