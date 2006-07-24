alter table ACCOUNTING_ENTRY add column KEY_RECEIPT int(11) DEFAULT NULL;
alter table ACCOUNTING_ENTRY add column ENTRY_TYPE varchar(100) NOT NULL;
