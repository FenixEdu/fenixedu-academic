alter table PAYMENT_CODE add column ENTITY_CODE varchar(5);

update PAYMENT_CODE set ENTITY_CODE = '20821';

alter table PAYMENT_CODE modify column ENTITY_CODE varchar(5) not null;


