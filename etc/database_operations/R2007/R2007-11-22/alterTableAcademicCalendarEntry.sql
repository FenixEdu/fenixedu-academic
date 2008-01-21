update ACADEMIC_CALENDAR_ENTRY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE' where CREDITS_ENTITY is not null and CREDITS_ENTITY = 'TEACHER';
update ACADEMIC_CALENDAR_ENTRY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForDepartmentAdmOfficeCE' where CREDITS_ENTITY is not null and CREDITS_ENTITY = 'DEPARTMENT_ADM_OFFICE';

alter table ACADEMIC_CALENDAR_ENTRY drop column CREDITS_ENTITY;
alter table ACADEMIC_CALENDAR_ENTRY drop column SEASON_TYPE;
alter table ACADEMIC_PERIOD drop column DEPARTMENT_ADM_OFFICE_CREDITS_PERIOD_INTERVAL;
alter table ACADEMIC_PERIOD drop column TEACHER_CREDITS_PERIOD_INTERVAL;