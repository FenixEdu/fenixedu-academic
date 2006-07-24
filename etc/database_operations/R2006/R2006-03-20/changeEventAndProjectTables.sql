DROP TABLE RESEARCH_EVENT;
DROP TABLE RESEARCH_EVENT_TRANSLATION;
drop table PROJECT;
drop table PROJECT_TITLE;
drop table PROJECT_ABSTRACT;
drop table PROJECT_KEYWORD;
drop table PROJECT_PROJECT_KEYWORD;

CREATE TABLE `RESEARCH_EVENT` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `NAME` longtext,
  `START_DATE` varchar(10),
  `END_DATE` varchar(10),
  `LOCATION` varchar(50),
  `FEE_OR_FREE` int(11),
  `TYPE` varchar(50),
  PRIMARY KEY  (`ID_INTERNAL`)
) Type=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PROJECT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `TITLE` longtext,
  `ABSTRACT` longtext,
  `START_DATE` varchar(10),
  `END_DATE` varchar(10),
  `STATUS` varchar(50),
  `TYPE` varchar(50),
  PRIMARY KEY (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;