


-- Inserted at 2010-03-05T17:27:52.975Z







-- Inserted at 2010-03-05T17:29:10.818Z







-- Inserted at 2010-03-05T17:33:09.004Z

alter table `QUALIFICATION` add column `OID_MODIFIED_BY` bigint(20);
alter table `QUALIFICATION` add column `WHEN_CREATED` text;
alter table `QUALIFICATION` add index (`OID_MODIFIED_BY`);


