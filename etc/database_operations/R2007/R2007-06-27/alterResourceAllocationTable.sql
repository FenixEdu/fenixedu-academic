alter table RESOURCE_ALLOCATION change column KEY_EXTENSION  KEY_MATERIAL int(11) default NULL;
alter table RESOURCE_ALLOCATION drop key KEY_EXTENSION;
alter table RESOURCE_ALLOCATION add key KEY_MATERIAL (KEY_MATERIAL);
alter table RESOURCE_ALLOCATION add column REASON text;

alter table RESOURCE_ALLOCATION change column BEGIN_DATE_TIME BEGIN_DATE_TIME timestamp default '0000-00-00 00:00:00';
alter table RESOURCE_ALLOCATION change column END_DATE_TIME END_DATE_TIME timestamp default '0000-00-00 00:00:00';