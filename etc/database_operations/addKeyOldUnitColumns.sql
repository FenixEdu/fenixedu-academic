alter table `DEPARTMENT` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `DEPARTMENT` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `DEPARTMENT` drop key `KEY_UNIT`;
alter table `DEPARTMENT` add key `KEY_UNIT` (`KEY_UNIT`);

alter table `EXTERNAL_PERSON` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `EXTERNAL_PERSON` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `EXTERNAL_PERSON` drop key `KEY_UNIT`;
alter table `EXTERNAL_PERSON` add key `KEY_UNIT` (`KEY_UNIT`);

alter table `CONTRACT` change column `KEY_WORKING_UNIT` `KEY_WORKING_OLD_UNIT` int(11) unsigned default NULL;
alter table `CONTRACT` change column `KEY_MAILING_UNIT` `KEY_MAILING_OLD_UNIT` int(11) unsigned default NULL;
alter table `CONTRACT` add column `KEY_MAILING_UNIT` int(11) unsigned default NULL;
alter table `CONTRACT` drop key `KEY_MAILING_UNIT`;
alter table `CONTRACT` add key `KEY_MAILING_UNIT` (`KEY_MAILING_UNIT`);
alter table `CONTRACT` add column `KEY_WORKING_UNIT` int(11) unsigned default NULL;
alter table `CONTRACT` drop key `KEY_WORKING_UNIT`;
alter table `CONTRACT` add key `KEY_WORKING_UNIT` (`KEY_WORKING_UNIT`);

alter table `NON_AFFILIATED_TEACHER` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `NON_AFFILIATED_TEACHER` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `NON_AFFILIATED_TEACHER` drop key `KEY_UNIT`;
alter table `NON_AFFILIATED_TEACHER` add key `KEY_UNIT` (`KEY_UNIT`);

alter table `FUNCTION` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `FUNCTION` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `FUNCTION` drop key `KEY_UNIT`;
alter table `FUNCTION` add key `KEY_UNIT` (`KEY_UNIT`);

alter table `COMPETENCE_COURSE` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `COMPETENCE_COURSE` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `COMPETENCE_COURSE` drop key `KEY_UNIT`;
alter table `COMPETENCE_COURSE` add key `KEY_UNIT` (`KEY_UNIT`);

alter table `DEGREE` change column `KEY_UNIT` `KEY_OLD_UNIT` int(11) unsigned default NULL;
alter table `DEGREE` add column `KEY_UNIT` int(11) unsigned default NULL;
alter table `DEGREE` drop key `KEY_UNIT`;
alter table `DEGREE` add key `KEY_UNIT` (`KEY_UNIT`);