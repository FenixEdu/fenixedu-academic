alter table ACCOUNTING_EVENT add column CLOSED tinyint(1) NOT NULL default '0';
alter table ACCOUNTING_EVENT drop column WHEN_NOTICED;
alter table ACCOUNTING_EVENT add column KEY_PAYED_EVENT tinyint(1) NOT NULL default '0';
alter table ACCOUNTING_EVENT add column KEY_REIMBURSED_EVENT tinyint(1) NOT NULL default '0';
