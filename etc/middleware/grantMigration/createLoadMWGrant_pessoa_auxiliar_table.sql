#copia de todas as pessoas para as quais existem referencias
CREATE TABLE mwgrant_pessoa_aux SELECT p.* FROM mwgrant_pessoa p inner join mwgrant_bolseiro b on p.codigoInterno=b.chavePessoa group by p.codigoInterno;
INSERT INTO mwgrant_pessoa_aux SELECT p.* FROM mwgrant_pessoa p inner join mwgrant_docente d on p.codigoInterno=d.chavePessoa group by p.codigoInterno;
INSERT INTO mwgrant_pessoa_aux SELECT p.* FROM mwgrant_pessoa p inner join mwgrant_habilitacao h on p.codigoInterno=h.chavePessoa group by p.codigoInterno;

#eliminar as entradas repetidas
CREATE TABLE mwgrant_migracao_pessoa SELECT p.* FROM mwgrant_pessoa_aux p group by p.codigoInterno;
DROP TABLE mwgrant_pessoa_aux;
