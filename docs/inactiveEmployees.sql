alter table ass_FUNCIONARIO add ACTIVE bit NOT NULL default 1;
select CONCAT("update ass_FUNCIONARIO set ACTIVE = 0 where codigoInterno=", f.codigoInterno,";") as "" 
from PERSON p 
		inner join ass_FUNCIONARIO f on f.chavePessoa = p.id_internal 
where username like "INA%";