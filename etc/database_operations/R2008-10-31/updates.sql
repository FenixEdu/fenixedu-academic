


-- Inserted at 2008-10-31T16:32:50.504Z

alter table SENDER add column KEY_UNIT int(11);
alter table SENDER add column OJB_CONCRETE_CLASS text;
alter table SENDER add index (KEY_UNIT);

alter table TEACHING_INQUIRY add column GLOBAL_CLASSIFICATION_OF_THIS_C_U int(11);
alter table TEACHING_INQUIRY add column WEAK_AND_STRONG_POINTS_OF_C_U_TEACHING_PROCESS text;
