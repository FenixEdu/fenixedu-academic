-- select concat('UPDATE ENROLMENT SET STATE = 1 WHERE ID_INTERNAL = ', e.ID_INTERNAL, ';') as "QQ"
-- from enrolment_evaluation ee
-- inner join enrolment e on ee.key_enrolment = e.id_internal
-- inner join curricular_course cc on e.key_curricular_course = cc.id_internal
-- inner join student_curricular_plan scp on e.key_student_curricular_plan = scp.id_internal
-- where 
-- cc.key_degree_curricular_plan = 48 and
-- cc.code = 'APD' and
-- (ee.grade is not null and ee.grade <> 'RE') and 
-- e.state = 3;

select concat('UPDATE ENROLMENT SET STATE = 1 WHERE ID_INTERNAL = ', e.ID_INTERNAL, ';') as "QQ"
from enrolment_evaluation ee
inner join enrolment e on ee.key_enrolment = e.id_internal
inner join curricular_course cc on e.key_curricular_course = cc.id_internal
inner join student_curricular_plan scp on e.key_student_curricular_plan = scp.id_internal
where 
(ee.grade is not null and ee.grade <> 'RE') and 
e.state = 3;
