INSERT INTO DEGREE
(NOME,
NAME_EN,
OJB_CONCRETE_CLASS) 
VALUES
("Curso de Unidades Isoladas",
"Standalone Curricular Units Degree",
"net.sourceforge.fenixedu.domain.EmptyDegree");

INSERT INTO DEGREE_CURRICULAR_PLAN
(NAME,
OJB_CONCRETE_CLASS,
KEY_DEGREE,
INITIAL_DATE_YEAR_MONTH_DAY,
DEGREE_DURATION,
MINIMAL_YEAR_FOR_OPTIONAL_COURSES,
CURRICULAR_STAGE,
APPLY_PREVIOUS_YEARS_ENROLMENT_RULE) 
VALUES
("Plano Curricular de Unidades Isoladas",
"net.sourceforge.fenixedu.domain.EmptyDegreeCurricularPlan",
(SELECT DEGREE.ID_INTERNAL 
FROM DEGREE 
WHERE DEGREE.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.EmptyDegree"),
"2008-09-01",
0,
NULL,
"APPROVED",
0);
