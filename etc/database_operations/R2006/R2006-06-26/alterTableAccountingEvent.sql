alter table ACCOUNTING_EVENT add column KEY_PARTY int(11) NOT NULL;
alter table ACCOUNTING_EVENT add column CLOSED tinyint(1) NOT NULL default '0';
alter table ACCOUNTING_EVENT drop column WHEN_NOTICED;
