SELECT E.* FROM ENROLMENT E
LEFT JOIN ENROLMENT_EVALUATION EE ON EE.KEY_ENROLMENT = E.ID_INTERNAL
WHERE EE.ID_INTERNAL IS NULL;

SELECT s.number,dcp.name,cc.code,cc.name,dcp.name,dcp.key_degree,E.* FROM ENROLMENT E
LEFT JOIN ENROLMENT_EVALUATION EE ON EE.KEY_ENROLMENT = E.ID_INTERNAL
inner join curricular_course cc on E.key_curricular_course = cc.id_internal
inner join degree_curricular_plan dcp on cc.key_degree_curricular_plan = dcp.id_internal
inner join student_curricular_plan scp on E.key_student_curricular_plan = scp.id_internal
inner join student s on scp.key_student = s.id_internal
WHERE EE.ID_INTERNAL IS NULL order by dcp.name;

-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

SELECT EE.* FROM ENROLMENT_EVALUATION EE
LEFT JOIN ENROLMENT E ON EE.KEY_ENROLMENT = E.ID_INTERNAL
WHERE E.ID_INTERNAL IS NULL;

-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

SELECT * FROM ENROLMENT E
LEFT JOIN CURRICULAR_COURSE CC ON E.KEY_CURRICULAR_COURSE = CC.ID_INTERNAL
WHERE
CC.ID_INTERNAL IS NULL;

-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

