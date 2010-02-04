
create table `DOCUMENT_PRINT_REQUEST` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `NEW_CONCLUSION_DATE_VALUE` longtext,
  `NEW_DEGREE_DESCRIPTION_VALUE` longtext,
  `NEW_GRADUATED_TITLE_VALUE` longtext,
  `OID` bigint(20),
  `OID_DOCUMENT_REQUEST` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_WHO_REQUESTED` bigint(20),
  `WHEN_REQUESTED` datetime NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_DOCUMENT_REQUEST),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (OID_WHO_REQUESTED)
) type=InnoDB, character set latin1 ;

