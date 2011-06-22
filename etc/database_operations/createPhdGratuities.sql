alter table `EVENT` add `OID_PHD_INDIVIDUAL_PROGRAM_PROCESS` bigint unsigned, add index (OID_PHD_INDIVIDUAL_PROGRAM_PROCESS);
alter table `EVENT` add `YEAR` int(11);
create table `PHD_GRATUITY_PAYMENT_PERIOD` (`DAY_LAST_PAYMENT` int(11), `OID` bigint unsigned, `MONTH_START` int(11), `DAY_START` int(11), `DAY_END` int(11), `OID_POSTING_RULE` bigint unsigned, `MONTH_END` int(11), `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `MONTH_LAST_PAYMENT` int(11), `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_POSTING_RULE), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
alter table `POSTING_RULE` add `GRATUITY` text, add `FINE_RATE` double;
