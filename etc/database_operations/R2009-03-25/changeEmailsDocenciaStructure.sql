-- Inserted at 2009-03-12T19:40:28.365Z

alter table `EXECUTION_COURSE` add column `KEY_SENDER` int(11);
alter table `EXECUTION_COURSE` add index (`KEY_SENDER`);
alter table `RESOURCE` add index (`KEY_ADMINISTRATIVE_OFFICE_PERMISSION_GROUP`);
alter table `SENDER` add column `KEY_COURSE` int(11);
alter table `SENDER` add index (`KEY_COURSE`);



-- Inserted at 2009-03-19T21:32:00.313Z

alter table `ADMINISTRATIVE_OFFICE_PERMISSION_GROUP` add index (`KEY_ROOT_DOMAIN_OBJECT`);
alter table `ADMINISTRATIVE_OFFICE_PERMISSION` add index (`KEY_ADMINISTRATIVE_OFFICE_PERMISSION_GROUP`);
alter table `ADMINISTRATIVE_OFFICE_PERMISSION` add index (`KEY_ROOT_DOMAIN_OBJECT`);
alter table `REPLY_TO` add column `OJB_CONCRETE_CLASS` text;

-- Inserted at 2009-03-24T13:31:40.387Z

alter table `PARTY` add column `KEY_REPLY_TO` int(11);
alter table `PARTY` add index (`KEY_REPLY_TO`);
alter table `REPLY_TO` add column `KEY_PERSON` int(11);
alter table `REPLY_TO` add index (`KEY_PERSON`);


