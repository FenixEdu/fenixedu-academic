select concat('UPDATE CURRICULAR_COURSE SET TYPE = 4 WHERE ID_INTERNAL = ', ID_INTERNAL, ';') as ""
from curricular_course
where name like 'Trabalho Final de Curso%' and key_degree_curricular_plan = 48;
