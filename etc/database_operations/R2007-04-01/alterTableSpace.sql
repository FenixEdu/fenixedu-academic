alter table SPACE add column NORMAL_CAPACITY int(11) default NULL;
alter table SPACE add column EXAM_CAPACITY int(11) default NULL;
alter table SPACE_OCCUPATION change column `ID_INTERNAL` `ID_INTERNAL` int(11) NOT NULL auto_increment;