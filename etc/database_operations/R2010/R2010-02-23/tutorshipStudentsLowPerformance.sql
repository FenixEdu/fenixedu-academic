


-- Inserted at 2010-02-22T12:27:20.853Z

alter table `ALERT` add index (`OID_WHO_CREATED`);
alter table `ENROLMENT_EVALUATION_LOG` add index (`OID_ROOT_DOMAIN_OBJECT`);
alter table `ERASMUS_STUDENT_DATA` add index (`OID_ROOT_DOMAIN_OBJECT`);





-- Inserted at 2010-02-22T19:03:48.960Z

alter table `QUEUE_JOB` add column `PRESCRIPTION_ENUM` text;


