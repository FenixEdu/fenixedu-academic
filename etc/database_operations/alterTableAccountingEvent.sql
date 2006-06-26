alter table ACCOUNTING_EVENT add column PROCESSED tinyint(1) NOT NULL default '0';
alter table ACCOUNTING_EVENT drop column WHEN_NOTICED;
