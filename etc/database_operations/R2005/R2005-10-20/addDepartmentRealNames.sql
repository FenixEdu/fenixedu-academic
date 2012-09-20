alter table DEPARTMENT add column REAL_NAME varchar(100) default '';

update DEPARTMENT set DEPARTMENT.NAME = 'SECCAO AUTONOMA DE ENGENHARIA NAVAL' where DEPARTMENT.NAME = 'SEC. AUTONOMA DE ENGENHARIA NAVAL';

update DEPARTMENT set REAL_NAME = 'Departamento de Física (DF)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE FISICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Electrotécnica e de Computadores (DEEC)' where DEPARTMENT.NAME = 'DEP. ENG. ELECT. E COMPUTADORES';
update DEPARTMENT set REAL_NAME = 'Departamento de Matemática (DM)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE MATEMATICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Química (DQ)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA QUIMICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Mecânica (DEM)' where DEPARTMENT.NAME = 'DEP. DE ENGENHARIA MECANICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia de Materiais (DEMAT)' where DEPARTMENT.NAME = 'DEPART. DE ENGENHARIA DE MATERIAIS';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Civil e Arquitectura (DECivil)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA CIVIL';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA MINAS';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Informática (DEI)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENG. INFORMATICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia e Gestão (DEG)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA E GESTAO';
update DEPARTMENT set REAL_NAME = 'Secção Autónoma de Engenharia Naval (SAEN)' where DEPARTMENT.NAME = 'SECCAO AUTONOMA DE ENGENHARIA NAVAL';