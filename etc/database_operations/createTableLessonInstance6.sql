update RESOURCE_ALLOCATION set BEGIN = '', END = '', KEY_PERIOD = NULL where OJB_CONCRETE_CLASS like "%LessonInstanceSpaceOccupation%" or OJB_CONCRETE_CLASS like "%GenericEventSpaceOccupation%" or OJB_CONCRETE_CLASS like "%WrittenEvaluationSpaceOccupation%";
update RESOURCE_ALLOCATION set BEGIN = '', END = '' where OJB_CONCRETE_CLASS like "%LessonSpaceOccupation%";

insert into RESOURCE_ALLOCATION (OJB_CONCRETE_CLASS, KEY_RESOURCE, KEY_LESSON_INSTANCE) select 'net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation', KEY_ROOM, KEY_LESSON_INSTANCE from SUMMARY where KEY_LESSON_INSTANCE is not null and KEY_ROOM is not null;

select concat('update LESSON_INSTANCE set KEY_LESSON_INSTANCE_SPACE_OCCUPATION = ' , ID_INTERNAL , ' where ID_INTERNAL = ' , KEY_LESSON_INSTANCE , ';') as "" from RESOURCE_ALLOCATION where KEY_LESSON_INSTANCE is not null and OJB_CONCRETE_CLASS like "%LessonInstanceSpaceOccupation%";

alter table SUMMARY drop column KEY_LESSON;

alter table RESOURCE_ALLOCATION drop column KEY_PERIOD; 
alter table RESOURCE_ALLOCATION drop column DAY_OF_WEEK;
alter table RESOURCE_ALLOCATION drop column START_TIME_DATE_HOUR_MINUTE_SECOND;
alter table RESOURCE_ALLOCATION drop column END_TIME_DATE_HOUR_MINUTE_SECOND;
alter table RESOURCE_ALLOCATION drop column DAILY_FREQUENCY_MARK_SUNDAY;
alter table RESOURCE_ALLOCATION drop column DAILY_FREQUENCY_MARK_SATURDAY;

delete from OJB_HL_SEQ;
