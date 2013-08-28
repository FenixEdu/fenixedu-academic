alter table `EXTERNAL_APPLICATION` add `URL` text, add `CLIENT_I_D` text;



-- Inserted at 2013-06-29T22:36:20.258Z

alter table `USER` add `OID_APP_USER_SESSION` bigint unsigned, add index (OID_APP_USER_SESSION);
alter table `EXTERNAL_APPLICATION` add `OID_APP_USER_SESSION` bigint unsigned, add index (OID_APP_USER_SESSION);
create table `APP_USER_SESSION` (`EXPIRATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `REFRES_TOKEN` text, `ACCESS_TOKEN` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set utf8;



-- Inserted at 2013-07-11T20:03:34.045+01:00

alter table `USER` add `OID_APP_USER_SESSION` bigint unsigned, add index (OID_APP_USER_SESSION);
create table `AUTH_SCOPE` (`OID` bigint unsigned, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `ENDPOINTS` text, `OID_APP` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_APP), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
create table `EXTERNAL_APPLICATION` (`OID` bigint unsigned, `OAUTH_KEY` text, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_AUTHOR` bigint unsigned, `OID_APP_USER_SESSION` bigint unsigned, `URL` text, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `CLIENT_I_D` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_AUTHOR), index (OID_APP_USER_SESSION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
create table `APP_USER_SESSION` (`EXPIRATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `REFRES_TOKEN` text, `ACCESS_TOKEN` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set utf8;
create table `USER_APP_PERMISSIONS` (`OID_EXTERNAL_APPLICATION` bigint unsigned, `OID_USER` bigint unsigned, primary key (OID_EXTERNAL_APPLICATION, OID_USER), index (OID_EXTERNAL_APPLICATION), index (OID_USER)) ENGINE=InnoDB, character set utf8;



-- Inserted at 2013-07-30T19:29:27.038+01:00

alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);



-- Inserted at 2013-07-31T16:29:55.606+01:00




-- Inserted at 2013-08-12T23:30:22.825+01:00

alter table `APP_USER_SESSION` add `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, add index (OID_ROOT_DOMAIN_OBJECT);



-- Inserted at 2013-08-19T16:24:50.891+01:00




-- Inserted at 2013-08-21T10:26:54.249Z

alter table `FILE` add `OID_RECOMENTATION` bigint unsigned, add `OID_GENERIC_APPLICATION` bigint unsigned, add index (OID_GENERIC_APPLICATION);
alter table `CANDIDACY_PERIOD` add `DESCRIPTION` text, add `PERIOD_NUMBER` int(11), add `TITLE` text;
create table `GENERIC_APPLICATION_RECOMENTATION` (`OID` bigint unsigned, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `INSTITUTION` text, `EMAIL` text, `OID_LETTER_OF_RECOMENTATION` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `CONFIRMATION_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);
create table `GENERIC_APPLICATION_COMMENT` (`OID` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `COMMENT` text, `CREATED` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
create table `GENERIC_APPLICATION` (`MARITAL_STATUS` text, `EMISSION_LOCATION_OF_DOCUMENT_ID` text, `OID` bigint unsigned, `AREA_CODE` text, `APPLICATION_NUMBER` text, `OID_NATIONALITY` bigint unsigned, `PROFESSION` text, `EMAIL` text, `FISCAL_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, `EMISSION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, `AREA_OF_AREA_CODE` text, `NAME` text, `AREA` text, `EXPIRATION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, `DOCUMENT_ID_NUMBER` text, `ADDRESS` text, `TELEPHONE_CONTACT` text, `CONFIRMATION_CODE` text, `ID_DOCUMENT_TYPE` text, `CANDIDATE_OBSERVATIONS` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `GENDER` text, `DATE_OF_BIRTH_YEAR_MONTH_DAY` text, `OID_GENERIC_APPLICATION_PERIOD` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, primary key (ID_INTERNAL), index (OID), index (OID_NATIONALITY), index (OID_GENERIC_APPLICATION_PERIOD), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;



-- Inserted at 2013-08-21T10:30:38.457Z

alter table `FILE` add `OID_RECOMENTATION` bigint unsigned, add `OID_GENERIC_APPLICATION` bigint unsigned, add index (OID_GENERIC_APPLICATION);
alter table `CANDIDACY_PERIOD` add `DESCRIPTION` text, add `PERIOD_NUMBER` int(11), add `TITLE` text;
create table `GENERIC_APPLICATION_RECOMENTATION` (`OID` bigint unsigned, `NAME` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `INSTITUTION` text, `EMAIL` text, `OID_LETTER_OF_RECOMENTATION` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `CONFIRMATION_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);
create table `GENERIC_APPLICATION_COMMENT` (`OID` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_GENERIC_APPLICATION` bigint unsigned, `COMMENT` text, `CREATED` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_GENERIC_APPLICATION), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
create table `GENERIC_APPLICATION` (`MARITAL_STATUS` text, `EMISSION_LOCATION_OF_DOCUMENT_ID` text, `OID` bigint unsigned, `AREA_CODE` text, `APPLICATION_NUMBER` text, `OID_NATIONALITY` bigint unsigned, `PROFESSION` text, `EMAIL` text, `FISCAL_CODE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, `EMISSION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, `AREA_OF_AREA_CODE` text, `NAME` text, `AREA` text, `EXPIRATION_DATE_OF_DOCUMENT_ID_YEAR_MONTH_DAY` text, `DOCUMENT_ID_NUMBER` text, `ADDRESS` text, `TELEPHONE_CONTACT` text, `CONFIRMATION_CODE` text, `ID_DOCUMENT_TYPE` text, `CANDIDATE_OBSERVATIONS` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `GENDER` text, `DATE_OF_BIRTH_YEAR_MONTH_DAY` text, `OID_GENERIC_APPLICATION_PERIOD` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, primary key (ID_INTERNAL), index (OID), index (OID_NATIONALITY), index (OID_GENERIC_APPLICATION_PERIOD), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;



-- Inserted at 2013-08-21T18:25:21.514Z

alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);



-- Inserted at 2013-08-21T20:17:08.707Z

alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);



-- Inserted at 2013-08-23T11:34:41.720Z

alter table `EXTERNAL_APPLICATION` add `SECRET` text;
alter table `APP_USER_SESSION` add `REFRESH_TOKEN` text, add `CODE_EXPIRATION_DATE` timestamp NULL default NULL, add `OID_USER` bigint unsigned, add `OID_APPLICATION` bigint unsigned, add `CODE` text, add index (OID_USER), add index (OID_APPLICATION);



-- Inserted at 2013-08-25T15:26:40.404Z

alter table `APP_USER_SESSION` add `DEVICE_ID` text;
