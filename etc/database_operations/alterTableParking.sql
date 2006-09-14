alter table PARKING_DOCUMENT modify column PARKING_DOCUMENT_TYPE VARCHAR(50) NOT NULL;
alter table PARKING_PARTY add column DRIVER_LICENSE_NUMBER int(11) unsigned;
alter table PARKING_REQUEST add column DRIVER_LICENSE_NUMBER int(11) unsigned;