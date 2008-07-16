alter table RESIDENCE_MONTH drop column PAYMENT_LIMIT_DAY;

alter table RESIDENCE_YEAR add column PAYMENT_LIMIT_DAY INTEGER;
alter table RESIDENCE_YEAR add column SINGLE_ROOM_VALUE varchar(255);
alter table RESIDENCE_YEAR add column DOUBLE_ROOM_VALUE varchar(255);
