
insert into ciapl.CLASS (ID_INTERNAL, NAME, SEMESTER, CURRICULAR_YEAR, KEY_DEGREE)
select codigoInterno, nome, semestre, anoCurricular, CEILING(chaveLicenciaturaExecucao / 2)
from fenix.turma;


