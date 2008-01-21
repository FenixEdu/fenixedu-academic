drop table PARKING_DOCUMENT;
alter table PARKING_PARTY drop column FIRST_CAR_MAKE ;
alter table PARKING_PARTY drop column FIRST_CAR_PLATE_NUMBER ;
alter table PARKING_PARTY drop column SECOND_CAR_PLATE_NUMBER ;
alter table PARKING_PARTY drop column SECOND_CAR_MAKE ;
alter table PARKING_REQUEST drop column SECOND_CAR_MAKE ;
alter table PARKING_REQUEST drop column SECOND_CAR_PLATE_NUMBER ;
alter table PARKING_REQUEST drop column FIRST_CAR_PLATE_NUMBER ;
alter table PARKING_REQUEST drop column FIRST_CAR_MAKE ;