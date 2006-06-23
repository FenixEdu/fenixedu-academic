alter table STUDENT_GROUP add unique index (GROUP_NUMBER, KEY_GROUPING);
alter table STUDENT_GROUP drop index GROUP_NUMBER_KEY_ATTENDS_SET;
alter table STUDENT_GROUP drop index KEY_ATTENDS_SET;
alter table STUDENT_GROUP drop column KEY_ATTENDS_SET;
