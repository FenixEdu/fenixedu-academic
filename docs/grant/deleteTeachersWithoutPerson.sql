#create table mwgrant_docentes_sem_pessoa (select * from mwgrant_docente where chavePessoa is null);

#select * from mwgrant_docentes_sem_pessoa d inner join mwgrant_contrato c on d.codigoInterno = c.chaveResponsavelActual;
#select * from mwgrant_docentes_sem_pessoa d inner join mwgrant_responsabilidade c on d.codigoInterno = c.chaveDocente;
#select * from mwgrant_docentes_sem_pessoa d inner join mwgrant_projecto c on d.codigoInterno = c.chaveDocente;
#select * from mwgrant_docentes_sem_pessoa d inner join mwgrant_orientacao c on d.codigoInterno = c.chaveDocente;

delete from mwgrant_docente where chavePessoa is null;

#select d.* from mwgrant_docente d
#left join mwgrant_pessoa p
#on p.codigoInterno=d.chavePessoa where p.codigoInterno is null;

delete from mwgrant_docente where codigoInterno=119;
delete from mwgrant_docente where codigoInterno=205;