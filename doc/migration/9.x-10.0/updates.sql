	create table `MESSAGE_RECIPIENT_TO` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_MESSAGE` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_MESSAGE), index (OID_PERSISTENT_GROUP), index (OID_MESSAGE)) ENGINE=InnoDB, character set utf8;
	alter table `MESSAGE` add `OID_MESSAGING_SYSTEM` bigint unsigned, add `PREFERRED_LOCALE` text, add `OID_CREATOR` bigint unsigned, add `TEXT_BODY` text, add `OID_DISPATCH_REPORT` bigint unsigned, add `OID_MESSAGING_SYSTEM_FROM_PENDING_DISPATCH` bigint unsigned, add `REPLY_TO` text, add `SINGLE_BCCS` text, add index (OID_MESSAGING_SYSTEM_FROM_PENDING_DISPATCH), add index (OID_MESSAGING_SYSTEM), add index (OID_CREATOR);
	

	create table `MESSAGE_DISPATCH_REPORT` (`STARTED_DELIVERY` datetime NULL default NULL, `INVALID_COUNT` int(11), `TOTAL_COUNT` int(11), `DELIVERED_COUNT` int(11), `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_QUEUE` bigint unsigned, `FINISHED_DELIVERY` datetime NULL default NULL, `OID_MESSAGE` bigint unsigned, `OID` bigint unsigned, `FAILED_COUNT` int(11), primary key (OID), index (OID_QUEUE), index (OID)) ENGINE=InnoDB, character set utf8;
	alter table `BENNU` add `OID_MESSAGING_SYSTEM` bigint unsigned;
	

	create table `MIME_MESSAGE_HANDLER` (`CC_ADDRESSES` text, `LOCALE` text, `OID_REPORT` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `TO_ADDRESSES` text, `OID` bigint unsigned, `BCC_ADDRESSES` text, primary key (OID), index (OID_REPORT), index (OID)) ENGINE=InnoDB, character set utf8;
	create table `MESSAGE_FILES` (`OID_GENERIC_FILE` bigint unsigned, `OID_MESSAGE` bigint unsigned, primary key (OID_GENERIC_FILE, OID_MESSAGE), index (OID_GENERIC_FILE), index (OID_MESSAGE)) ENGINE=InnoDB, character set utf8;
	create table `MESSAGE_TEMPLATE` (`OID_MESSAGING_SYSTEM` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `SUBJECT` text, `OID` bigint unsigned, `ID` text, `HTML_BODY` text, `TEXT_BODY` text, primary key (OID), index (OID_MESSAGING_SYSTEM), index (OID)) ENGINE=InnoDB, character set utf8;
	create table `SENDER_PERSISTENT_GROUP_RECIPIENTS` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_SENDER` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_SENDER), index (OID_PERSISTENT_GROUP), index (OID_SENDER)) ENGINE=InnoDB, character set utf8;
	alter table `PARTY` add `OID_RESEARCHSENDER` bigint unsigned;
	

	create table `MESSAGE_RECIPIENT_CC` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_MESSAGE` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_MESSAGE), index (OID_PERSISTENT_GROUP), index (OID_MESSAGE)) ENGINE=InnoDB, character set utf8;
	create table `EMAIL_BLACKLIST` (`BLACKLIST` text, `OID_MESSAGING_SYSTEM` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID` bigint unsigned, primary key (OID), index (OID)) ENGINE=InnoDB, character set utf8;
	create table `MESSAGING_SYSTEM` (`OID_PERSISTENT_OPTED_OUT_GROUP` bigint unsigned, `OID_PERSISTENT_OPT_OUT_AVAILABLE_GROUP` bigint unsigned, `OID_SYSTEM_SENDER` bigint unsigned, `OID_BENNU` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID` bigint unsigned, `OID_BLACKLIST` bigint unsigned, primary key (OID), index (OID)) ENGINE=InnoDB, character set utf8;
	create table `MESSAGE_RECIPIENT_BCC` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_MESSAGE` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_MESSAGE), index (OID_PERSISTENT_GROUP), index (OID_MESSAGE)) ENGINE=InnoDB, character set utf8;
	

	alter table `GENERIC_FILE` add `OID_SENDER` bigint unsigned, add index (OID_SENDER);
	alter table `PERSISTENT_GROUP` add `OID_MESSAGING_SYSTEM_OPTED_OUT` bigint unsigned, add `OID_MESSAGING_SYSTEM_OPT_OUT_AVAILABLE` bigint unsigned, add `OID_ROOT_FOR_NAMED_GROUPS` bigint unsigned, add index (OID_ROOT_FOR_NAMED_GROUPS);
	alter table `SENDER` add `OID_ROOT_FOR_SYSTEM_SENDER` bigint unsigned, add `OID_MESSAGING_SYSTEM` bigint unsigned, add `OID_MEMBER_GROUP` bigint unsigned, add `NAME` text, add `POLICY` text, add `ADDRESS` text, add `REPLY_TO` text, add `OID_RESEARCHUNIT` bigint unsigned, add `HTML_ENABLED` tinyint(1), add index (OID_MESSAGING_SYSTEM), add index (OID_MEMBER_GROUP);