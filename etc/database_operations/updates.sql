alter table `FILE` add `OID_GENERIC_APPLICATION` bigint unsigned, add index (OID_GENERIC_APPLICATION);
alter table `CANDIDACY_PERIOD` add `DESCRIPTION` text, add `PERIOD_NUMBER` int(11), add `TITLE` text;
create table `GENERIC_APPLICATION` (`OID` bigint unsigned, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `EMAIL` text, `OID_GENERIC_APPLICATION_PERIOD` bigint unsigned, `APPLICATION_NUMBER` text, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `CONFIRMATION_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION_PERIOD), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;



-- Inserted at 2013-07-28T21:51:09.305+01:00

alter table `GENERIC_APPLICATION` add `EMISSION_LOCATION_OF_DOCUMENT_ID` text, add `EMISSION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, add `MARITAL_STATUS` text, add `AREA_OF_AREA_CODE` text, add `AREA` text, add `EXPIRATION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, add `ADDRESS` text, add `DOCUMENT_ID_NUMBER` text, add `AREA_CODE` text, add `TELEPHONE_CONTACT` text, add `ID_DOCUMENT_TYPE` text, add `GENDER` text, add `OID_NATIONALITY` bigint unsigned, add `DATE_OF_BIRTH_YEAR_MONTH_DAY` text, add `PROFESSION` text, add `FISCAL_CODE` text, add index (OID_NATIONALITY);



-- Inserted at 2013-07-29T00:49:34.859+01:00

create table `GENERIC_APPLICATION_RECOMENTATION` (`OID` bigint unsigned, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `INSTITUTION` text, `EMAIL` text, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `CONFIRMATION_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
create table `GENERIC_APPLICATION_COMMENT` (`OID` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `COMMENT` text, `CREATED` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
alter table `GENERIC_APPLICATION` add `CANDIDATE_OBSERVATIONS` text;



-- Inserted at 2013-07-29T19:06:09.213+01:00

alter table `FILE` add `OID_RECOMENTATION` bigint unsigned;
alter table `GENERIC_APPLICATION_RECOMENTATION` add `OID_LETTER_OF_RECOMENTATION` bigint unsigned;
