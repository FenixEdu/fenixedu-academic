ALTER TABLE DEGREE_CONTEXT CHANGE COLUMN PERIOD_TYPE ACADEMIC_PERIOD text;
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesters:1" WHERE ACADEMIC_PERIOD = "SEMESTER";
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:1" WHERE ACADEMIC_PERIOD = "YEAR";
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:2" WHERE ACADEMIC_PERIOD = "TWO_YEAR";
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:3" WHERE ACADEMIC_PERIOD = "THREE_YEAR";
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:5" WHERE ACADEMIC_PERIOD = "FIVE_YEAR";
UPDATE DEGREE_CONTEXT SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesters:1" WHERE ACADEMIC_PERIOD = "TRIMESTER";

ALTER TABLE CURRICULAR_PERIOD CHANGE COLUMN PERIOD_TYPE ACADEMIC_PERIOD varchar(255) NOT NULL default '';
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesters:1" WHERE ACADEMIC_PERIOD = "SEMESTER";
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:1" WHERE ACADEMIC_PERIOD = "YEAR";
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:2" WHERE ACADEMIC_PERIOD = "TWO_YEAR";
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:3" WHERE ACADEMIC_PERIOD = "THREE_YEAR";
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:5" WHERE ACADEMIC_PERIOD = "FIVE_YEAR";
UPDATE CURRICULAR_PERIOD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesters:1" WHERE ACADEMIC_PERIOD = "TRIMESTER";

ALTER TABLE CURRICULAR_RULE CHANGE COLUMN CURRICULAR_PERIOD_TYPE ACADEMIC_PERIOD varchar(255) default '';
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesters:1" WHERE ACADEMIC_PERIOD = "SEMESTER";
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:1" WHERE ACADEMIC_PERIOD = "YEAR";
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:2" WHERE ACADEMIC_PERIOD = "TWO_YEAR";
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:3" WHERE ACADEMIC_PERIOD = "THREE_YEAR";
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:5" WHERE ACADEMIC_PERIOD = "FIVE_YEAR";
UPDATE CURRICULAR_RULE SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesters:1" WHERE ACADEMIC_PERIOD = "TRIMESTER";

ALTER TABLE COMPETENCE_COURSE_LOAD CHANGE COLUMN CURRICULAR_PERIOD_TYPE ACADEMIC_PERIOD varchar(255) default '';
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesters:1" WHERE ACADEMIC_PERIOD = "SEMESTER";
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:1" WHERE ACADEMIC_PERIOD = "YEAR";
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:2" WHERE ACADEMIC_PERIOD = "TWO_YEAR";
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:3" WHERE ACADEMIC_PERIOD = "THREE_YEAR";
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYears:5" WHERE ACADEMIC_PERIOD = "FIVE_YEAR";
UPDATE COMPETENCE_COURSE_LOAD SET ACADEMIC_PERIOD = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesters:1" WHERE ACADEMIC_PERIOD = "TRIMESTER";
