-- SELECT CONCAT('INSERT INTO EXECUTION_YEAR VALUES (0,"', DEYS.enrolmentYear, '/', DEYS.enrolmentYear + 1,'","CL");') AS ""
-- FROM mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER DEYS WHERE curricularcoursesemester = "1";

SELECT CONCAT('INSERT INTO EXECUTION_YEAR VALUES (0,"', DEYS.enrolmentYear, '/', DEYS.enrolmentYear + 1,'","CL","',
CONCAT_WS('-', DEYS.enrolmentYear, '08', '01'), '","',
CONCAT_WS('-', DEYS.enrolmentYear + 1, '07', '31'), '");') AS ""
FROM mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER DEYS WHERE curricularcoursesemester = "1";
