-- ATENTION!!!!!!!!
-- THIS SCRIPT GIVES CORRECT RESULTS ONLY IF THE SCRIPT "update_enrolment_set_state1.sql" IS RUNED BEFORE THIS ONE.

SELECT CONCAT('UPDATE ENROLMENT SET STATE = 2 WHERE ID_INTERNAL = ', E.ID_INTERNAL, ';') AS ""
FROM ENROLMENT E
INNER JOIN CURRICULAR_COURSE CC ON E.KEY_CURRICULAR_COURSE = CC.ID_INTERNAL
INNER JOIN DEGREE_CURRICULAR_PLAN DCP ON CC.KEY_DEGREE_CURRICULAR_PLAN = DCP.ID_INTERNAL
INNER JOIN DEGREE D ON DCP.KEY_DEGREE = D.ID_INTERNAL
INNER JOIN ENROLMENT_EVALUATION EE ON EE.KEY_ENROLMENT = E.ID_INTERNAL
WHERE
D.TYPE_DEGREE = 1 AND
EE.GRADE = 'RE' AND
E.STATE <> 2 AND
E.STATE <> 5 AND
E.STATE <> 1;
