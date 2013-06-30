alter table `EXTERNAL_APPLICATION` add `URL` text, add `CLIENT_I_D` text;



-- Inserted at 2013-06-29T22:36:20.258Z

alter table `USER` add `OID_APP_USER_SESSION` bigint unsigned, add index (OID_APP_USER_SESSION);
alter table `EXTERNAL_APPLICATION` add `OID_APP_USER_SESSION` bigint unsigned, add index (OID_APP_USER_SESSION);
create table `APP_USER_SESSION` (`EXPIRATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `REFRES_TOKEN` text, `ACCESS_TOKEN` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set utf8;
