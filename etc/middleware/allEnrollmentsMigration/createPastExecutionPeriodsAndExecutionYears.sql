DROP TABLE IF EXISTS mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER;
CREATE TABLE mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER
SELECT DISTINCT enrolmentYear, curricularCourseSemester FROM mw_ENROLMENT ORDER BY enrolmentYear;

SELECT CONCAT('INSERT INTO EXECUTION_YEAR VALUES (0,"', DEYS.enrolmentYear, '/', DEYS.enrolmentYear + 1,'","CL");') AS ""
FROM mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER DEYS WHERE curricularcoursesemester = "1";

SELECT CONCAT('INSERT INTO EXECUTION_PERIOD VALUES (0,"', DEYS.curricularCourseSemester, ' Semestre",',
EY.ID_INTERNAL, ',"CL",', DEYS.curricularCourseSemester, ',"',
CASE DEYS.curricularCourseSemester WHEN 1 THEN CONCAT_WS('-', DEYS.enrolmentYear, '09', '01') ELSE
CONCAT_WS('-', DEYS.ENROLMENTYEAR + 1, '03', '01') END, '","',
CASE DEYS.curricularCourseSemester WHEN 1 THEN CONCAT_WS('-', DEYS.enrolmentYear, '12', '31') ELSE
CONCAT_WS('-', DEYS.enrolmentYear + 1, '07', '31') END, '");') AS ""
FROM mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER DEYS
INNER JOIN EXECUTION_YEAR EY ON DEYS.enrolmentYear = CONCAT(EY.YEAR, '/', EY.YEAR + 1)
WHERE EY.YEAR < '2002/2003';
