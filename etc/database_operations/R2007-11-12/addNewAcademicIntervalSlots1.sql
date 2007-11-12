alter table EXECUTION_PERIOD add column EXECUTION_INTERVAL text;
alter table EXECUTION_YEAR add column EXECUTION_INTERVAL text;

alter table ACADEMIC_CALENDAR_ENTRY add column REFERENCE_KEY int(11);

insert into ACADEMIC_CALENDAR_ENTRY (TITLE, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT_FOR_ROOT_ENTRIES, KEY_ROOT_DOMAIN_OBJECT) values ('pt35:Calendário Académico da Instituição', 'net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry', 1, 1);

select concat('insert into ACADEMIC_CALENDAR_ENTRY (TITLE, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, BEGIN, END, REFERENCE_KEY) values ("' , concat('pt',length(YEAR),':',YEAR) , '","net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE",1,"' , concat(BEGIN_DATE_YEAR_MONTH_DAY,' 00:00:00') , '","' , concat(END_DATE_YEAR_MONTH_DAY,' 00:00:00') , '",' , ID_INTERNAL ,');') as "" from EXECUTION_YEAR;

select concat('update ACADEMIC_CALENDAR_ENTRY set KEY_PARENT_ENTRY = ' , ID_INTERNAL , ' where OJB_CONCRETE_CLASS like "%Year%";') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%Root%';
