alter table RESOURCE_ALLOCATION add column DISTANCE varchar(250) default '';
alter table RESOURCE_ALLOCATION add column AMOUNT_CHARGED varchar(250) default '';
alter table RESOURCE add column ALLOCATION_COST_MULTIPLIER varchar(250) default '';