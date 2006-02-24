alter table `DEPARTMENT` drop column `KEY_OLD_UNIT`;

alter table `EXTERNAL_PERSON` drop column `KEY_OLD_UNIT`;

alter table `CONTRACT` drop column `KEY_WORKING_OLD_UNIT`;
alter table `CONTRACT` drop column `KEY_MAILING_OLD_UNIT`;

alter table `NON_AFFILIATED_TEACHER` drop column `KEY_OLD_UNIT`;

alter table `FUNCTION` drop column `KEY_OLD_UNIT`;

alter table `COMPETENCE_COURSE` drop column `KEY_OLD_UNIT`;

alter table `DEGREE` drop column `KEY_OLD_UNIT`;

drop table `UNIT`;
drop table `UNIT_UNIT`;