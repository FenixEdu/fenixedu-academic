SELECT E.* FROM ENROLMENT E
LEFT JOIN ENROLMENT_EVALUATION EE ON EE.KEY_ENROLMENT = E.ID_INTERNAL
WHERE EE.ID_INTERNAL IS NULL;

SELECT cc.code,cc.name,dcp.name,dcp.key_degree,E.* FROM ENROLMENT E
LEFT JOIN ENROLMENT_EVALUATION EE ON EE.KEY_ENROLMENT = E.ID_INTERNAL
inner join curricular_course cc on E.key_curricular_course = cc.id_internal
inner join degree_curricular_plan dcp on cc.key_degree_curricular_plan = dcp.id_internal
WHERE EE.ID_INTERNAL IS NULL;

SELECT EE.* FROM ENROLMENT_EVALUATION EE
LEFT JOIN ENROLMENT E ON EE.KEY_ENROLMENT = E.ID_INTERNAL
WHERE E.ID_INTERNAL IS NULL;
