truncate ciapl.DEGREE;
truncate ciapl.DEGREE_CURRICULAR_PLAN;

insert into ciapl.DEGREE (ID_INTERNAL,CODE,	NAME,TYPE_DEGREE)
select codigoInterno, sigla, nome, 1 as tipoLicenciatura from fenix.licenciatura;

insert into ciapl.DEGREE_CURRICULAR_PLAN (NAME, CODE, KEY_DEGREE)
select NAME, CODE, ID_INTERNAL 
from ciapl.DEGREE


