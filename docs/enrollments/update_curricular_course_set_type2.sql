select concat('UPDATE CURRICULAR_COURSE SET TYPE = 2 WHERE ID_INTERNAL = ', ID_INTERNAL, ';') as ""
from curricular_course
where name like 'Opção%' and key_degree_curricular_plan = 48;
