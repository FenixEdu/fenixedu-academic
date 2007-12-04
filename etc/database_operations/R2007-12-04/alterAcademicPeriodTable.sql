alter table ACADEMIC_PERIOD add column BEGIN_DATE_YEAR_MONTH_DAY varchar(10) not null;
alter table ACADEMIC_PERIOD add column END_DATE_YEAR_MONTH_DAY varchar(10) not null;

select concat('update ACADEMIC_PERIOD set BEGIN_DATE_YEAR_MONTH_DAY = "' , SUBSTRING(BEGIN,1,10) , '", END_DATE_YEAR_MONTH_DAY = "' , SUBSTRING(END,1,10) , '" where EXECUTION_INTERVAL like "' , CONCAT(OJB_CONCRETE_CLASS, ':', ID_INTERNAL) , '%" ;') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%AcademicSemester%' or OJB_CONCRETE_CLASS like '%AcademicYear%';
