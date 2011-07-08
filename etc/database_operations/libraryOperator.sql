drop table SPACE_ATTENDANCES;
drop table SPACES_HAVE_ATTENDANCE;
drop table SPACES_HAVE_ATTENDANCE_HISTORY;

alter table `RESOURCE` add `OID_CURRENT_ATTENDANCE` bigint unsigned;
create table `SPACE_ATTENDANCES` (`OID_VISITED_LIBRARY_PLACE` bigint unsigned, `OID` bigint unsigned, `RESPONSIBLE_FOR_EXIT_IST_USERNAME` text, `EXIT_TIME` timestamp NULL default NULL, `ENTRANCE_TIME` timestamp NULL default NULL, `PERSON_IST_USERNAME` text, `OID_OCCUPIED_LIBRARY_PLACE` bigint unsigned, `RESPONSIBLE_FOR_ENTRANCE_IST_USERNAME` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_VISITED_LIBRARY_PLACE)) ENGINE=InnoDB, character set latin1;
