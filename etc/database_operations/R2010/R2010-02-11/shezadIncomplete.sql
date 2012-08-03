


-- Inserted at 2010-02-11T21:31:00.093Z

alter table `INQUIRIES_TEACHER` add column `OID_PROFESSORSHIP` bigint(20);
alter table `INQUIRIES_TEACHER` add index (`OID_PROFESSORSHIP`);


