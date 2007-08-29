alter table SUMMARY change column LAST_MODIFIED_DATE_DATE_TIME LAST_MODIFIED_DATE_DATE_TIME datetime;

alter table LESSON_INSTANCE change column BEGIN_DATE_TIME BEGIN_DATE_TIME datetime;
alter table LESSON_INSTANCE change column END_DATE_TIME END_DATE_TIME datetime;

alter table RESOURCE_ALLOCATION change column BEGIN_DATE_TIME BEGIN_DATE_TIME datetime;
alter table RESOURCE_ALLOCATION change column END_DATE_TIME END_DATE_TIME datetime;

alter table ACADEMIC_CALENDAR_ENTRY change column BEGIN BEGIN datetime;
alter table ACADEMIC_CALENDAR_ENTRY change column END END datetime;

alter table IDENTIFICATION change column LAST_MODIFIED_DATE LAST_MODIFIED_DATE datetime;
alter table IDENTIFICATION change column BEGIN_DATE_DATE_TIME BEGIN_DATE_DATE_TIME datetime;
alter table IDENTIFICATION change column END_DATE_DATE_TIME END_DATE_DATE_TIME datetime;

alter table LESSON drop column KEY_EXECUTION_COURSE;
alter table LESSON drop column KEY_EXECUTION_PERIOD;
