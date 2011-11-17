create table `PHD_GRATUITY_PRICE_QUIRK` (
        `OID` bigint unsigned, 
        `YEAR` int(11), 
        `OID_POSTING_RULE` bigint unsigned, 
        `GRATUITY` text, 
        `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, 
        `ID_INTERNAL` int(11) NOT NULL auto_increment, 
        primary key (ID_INTERNAL), 
        index (OID), 
        index (OID_POSTING_RULE), 
        index (OID_ROOT_DOMAIN_OBJECT)
) ENGINE=InnoDB, character set latin1;

alter table `EXEMPTION` add `OID_FCT_SCHOLARSHIP_PHD_GRATUITY_CONTRIBUITION_EVENT` bigint unsigned;
alter table `EVENT` add `OID_PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION` bigint unsigned;
alter table `EVENT` CHANGE COLUMN `OID_PERSON` `OID_PARTY` bigint(20) unsigned;

