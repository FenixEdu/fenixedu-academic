alter table `PROFESSORSHIP` add `OID_PERMISSIONS` bigint unsigned;
create table `PROFESSORSHIP_PERMISSIONS` (`OID` bigint unsigned, `PERSONALIZATION` tinyint(1), `STUDENTS` tinyint(1), `EVALUATION_SPECIFIC` tinyint(1), `BIBLIOGRAFY` tinyint(1), `SUMMARIES` tinyint(1), `EVALUATION_METHOD` tinyint(1), `SECTIONS` tinyint(1), `EVALUATION_WORKSHEETS` tinyint(1), `EVALUATION_TESTS` tinyint(1), `SHIFT` tinyint(1), `EVALUATION_EXAMS` tinyint(1), `ANNOUNCEMENTS` tinyint(1), `OID_PROFESSORSHIP` bigint unsigned, `EVALUATION_PROJECT` tinyint(1), `GROUPS` tinyint(1), `WORKSHEETS` tinyint(1), `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `SITE_ARCHIVE` tinyint(1), `ID_INTERNAL` int(11) NOT NULL auto_increment, `PLANNING` tinyint(1), primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) type=InnoDB, character set latin1;
alter table `FF$PERSISTENT_ROOT` add index (OID);
alter table `PROFESSORSHIP_PERMISSIONS` add `EVALUATION_FINAL` tinyint(1);

