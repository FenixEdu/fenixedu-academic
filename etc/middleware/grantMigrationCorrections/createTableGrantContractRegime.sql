#
# Table structure for table 'grant_contract_regime'
#

DROP TABLE IF EXISTS GRANT_CONTRACT_REGIME;
CREATE TABLE GRANT_CONTRACT_REGIME (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  STATE int(2) unsigned NOT NULL default '0',
  DATE_BEGIN_CONTRACT date default NULL,
  DATE_END_CONTRACT date default NULL,
  DATE_SEND_DISPATCH_CC date default NULL,
  DATE_DISPATCH_CC date default NULL,
  DATE_SEND_DISPATCH_CD date default NULL,
  DATE_DISPATCH_CD date default NULL,
  DATE_ACCEPT_TERM date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;