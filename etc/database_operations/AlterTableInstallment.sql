alter table INSTALLMENT add column PENALTY_APPLIABLE tinyint(1);
alter table INSTALLMENT modify PENALTY_PERCENTAGE varchar(255) null;

alter table INSTALLMENT add column ECTS_FOR_AMOUNT varchar(255) null;
