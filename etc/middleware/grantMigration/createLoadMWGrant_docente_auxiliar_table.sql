#migracao de todos os docentes que existem como pessoa
CREATE TABLE mwgrant_migracao_docente SELECT d.* FROM mwgrant_docente d inner join mwgrant_migracao_pessoa p on d.chavePessoa=p.codigoInterno;
alter table mwgrant_migracao_docente add new_id_internal integer default 0;
