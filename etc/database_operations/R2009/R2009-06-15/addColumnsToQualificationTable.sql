
alter table `QUALIFICATION` add column `ATTENDED_BEGIN` text;
alter table `QUALIFICATION` add column `ATTENDED_END` text;
alter table `QUALIFICATION` add index (`KEY_CONCLUSION_EXECUTION_YEAR`), add index (`OID_CONCLUSION_EXECUTION_YEAR`);
alter table `QUALIFICATION` add index (`KEY_INDIVIDUAL_CANDIDACY`), add index (`OID_INDIVIDUAL_CANDIDACY`);


