alter table STUDENT_CURRICULAR_PLAN add column KEY_CURRICULUM_GROUP int(11) not null default 0;
alter table CURRICULUM_MODULE add column KEY_NEW_STUDENT_CURRICULAR_PLAN int(11) not null default 0;