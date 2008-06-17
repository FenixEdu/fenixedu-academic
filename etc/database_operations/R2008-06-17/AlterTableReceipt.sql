alter table RECEIPT add column RECEIPT_DATE date not null;

update RECEIPT set RECEIPT_DATE = WHEN_CREATED;


alter table RECEIPT add column CONTRIBUTOR_NAME varchar(255) null;


alter table RECEIPT modify column KEY_CONTRIBUTOR_PARTY int(11) null;


alter table RECEIPT modify KEY_EMPLOYEE int(11) NULL;




