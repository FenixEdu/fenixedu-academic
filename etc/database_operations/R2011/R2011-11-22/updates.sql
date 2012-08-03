alter table PROJECT_ACCESS change column KEY_PROJECT KEY_PROJECT varchar(255);

alter table PROJECT_ACCESS change column IT_PROJECT IT_PROJECT tinyint(1) DEFAULT '0';



-- Inserted at 2011-11-22T19:23:08.836Z

create table `STRIKE_DAY` (`OID` bigint unsigned, `DATE` text, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
