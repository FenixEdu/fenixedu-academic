select concat('DELETE FROM ENROLMENT_EVALUATION WHERE ID_INTERNAL = ', ee2.id_internal, ';') as "QQ"
from enrolment e
inner join enrolment_evaluation ee1 on ee1.key_enrolment = e.id_internal
inner join enrolment_evaluation ee2 on ee2.key_enrolment = e.id_internal
inner join curricular_course cc on e.key_curricular_course = cc.id_internal
where
cc.key_degree_curricular_plan = 48 and
cc.code = 'APD' and
ee1.grade is not null and
ee2.grade is null and
e.state = 3;
