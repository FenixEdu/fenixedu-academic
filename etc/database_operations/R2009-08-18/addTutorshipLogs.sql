


-- Inserted at 2009-08-06T18:44:58.526+01:00
alter table `TUTORSHIP` add column `KEY_TUTORSHIP_LOG` int(11);
alter table `TUTORSHIP` add column `OID_TUTORSHIP_LOG` bigint(20);
alter table `TUTORSHIP` add index (`KEY_TUTORSHIP_LOG`), add index (`OID_TUTORSHIP_LOG`);


create table `TUTORSHIP_LOG` (
  `ANNOTATIONS` longtext,
  `COUNTS_WITH_SUPPORT` tinyint(1),
  `DIFFICULTIES_OR_SPECIAL_LIMITATIONS` tinyint(1),
  `HOW_MANY_REUNIONS` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_TUTORSHIP` int(11),
  `MOTIVATION` text,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_TUTORSHIP` bigint(20),
  `OPTION_DEGREE` int(11),
  `RELATIVES_SUPPORT` tinyint(1),
  `SPACE_TO_VALIDATE_STUDENTS_REGISTRATION` longtext,
  `WISHES_TUTOR` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_TUTORSHIP),
  index (OID_TUTORSHIP)
) type=InnoDB, character set latin1 ;

