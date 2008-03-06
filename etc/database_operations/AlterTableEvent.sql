alter table EVENT add column NUMBER_OF_DELAY_DAYS int(11) NULL default NULL;
alter table EVENT add column KEY_EXECUTION_PERIOD int(11) NULL default NULL;
alter table EVENT add index (KEY_EXECUTION_PERIOD);
