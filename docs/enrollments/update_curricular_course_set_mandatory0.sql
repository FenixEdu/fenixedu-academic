select concat('UPDATE CURRICULAR_COURSE SET MANDATORY = 0 WHERE ID_INTERNAL = ', cc.ID_INTERNAL, ';') as ""
from mw_disciplinas_ileec mwdileec
inner join curricular_course cc on mwdileec.codigo_disciplina = cc.code
inner join degree_curricular_plan dcp on cc.key_degree_curricular_plan = dcp.id_internal
where (dcp.id_internal = 48 and mwdileec.insc_obrigatoria = 0) or (dcp.id_internal = 48 and cc.name like 'Trabalho Final de Curso%');
