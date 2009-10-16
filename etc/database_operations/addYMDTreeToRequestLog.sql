


-- Inserted at 2009-09-22T14:35:54.833+01:00

alter table `REQUEST_LOG` add column `KEY_DAY` int(11);
alter table `REQUEST_LOG` add column `OID_DAY` bigint(20);
alter table `REQUEST_LOG` add column `POST` tinyint(1);
alter table `REQUEST_LOG` add index (`KEY_DAY`), add index (`OID_DAY`);

create table `REQUEST_LOG_DAY` (
  `DAY_OF_MONTH` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_MONTH` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `OID` bigint(20),
  `OID_MONTH` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_MONTH),
  index (OID_MONTH),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `REQUEST_LOG_MONTH` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_YEAR` int(11),
  `MONTH_OF_YEAR` int(11),
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_YEAR` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_YEAR),
  index (OID_YEAR)
) type=InnoDB, character set latin1 ;

create table `REQUEST_LOG_YEAR` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `YEAR` int(11),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;


alter table `REQUEST_LOG_DAY` add column `KEY_NEXT` int(11);
alter table `REQUEST_LOG_DAY` add column `KEY_PREVIOUS` int(11);
alter table `REQUEST_LOG_DAY` add column `OID_NEXT` bigint(20);
alter table `REQUEST_LOG_DAY` add column `OID_PREVIOUS` bigint(20);
alter table `REQUEST_LOG_DAY` add index (`KEY_NEXT`), add index (`OID_NEXT`);
alter table `REQUEST_LOG_DAY` add index (`KEY_PREVIOUS`), add index (`OID_PREVIOUS`);
alter table `REQUEST_LOG_DAY` add column `KEY_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY` int(11);
alter table `REQUEST_LOG_DAY` add column `OID_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY` bigint(20);
alter table `REQUEST_LOG_DAY` add index (`KEY_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY`), add index (`OID_ROOT_DOMAIN_OBJECT_REQUEST_LOG_DAY`);
alter table `ROOT_DOMAIN_OBJECT` add column `KEY_MOST_RECENT_REQUEST_LOG_DAY` int(11);
alter table `ROOT_DOMAIN_OBJECT` add column `OID_MOST_RECENT_REQUEST_LOG_DAY` bigint(20);
alter table `ROOT_DOMAIN_OBJECT` add index (`KEY_MOST_RECENT_REQUEST_LOG_DAY`), add index (`OID_MOST_RECENT_REQUEST_LOG_DAY`);

