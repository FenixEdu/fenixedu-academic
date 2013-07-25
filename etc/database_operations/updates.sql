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
