SELECT CONCAT('INSERT INTO EXECUTION_PERIOD VALUES (0,"', DEYS.curricularCourseSemester, ' Semestre",',
EY.ID_INTERNAL, ',"CL",', DEYS.curricularCourseSemester, ',"',
CASE DEYS.curricularCourseSemester WHEN 1 THEN CONCAT_WS('-', DEYS.enrolmentYear, '09', '01') ELSE
CONCAT_WS('-', DEYS.ENROLMENTYEAR + 1, '03', '01') END, '","',
CASE DEYS.curricularCourseSemester WHEN 1 THEN CONCAT_WS('-', DEYS.enrolmentYear, '12', '31') ELSE
CONCAT_WS('-', DEYS.enrolmentYear + 1, '07', '31') END, '");') AS ""
FROM mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER DEYS
INNER JOIN EXECUTION_YEAR EY ON DEYS.enrolmentYear = CONCAT(EY.YEAR, '/', EY.YEAR + 1);
-- WHERE EY.YEAR < '2002/2003';
