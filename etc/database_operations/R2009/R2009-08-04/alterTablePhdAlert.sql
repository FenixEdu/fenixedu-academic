
alter table `PHD_ALERT` add column `KEY_CANDIDACY_HASH_CODE` int(11);
alter table `PHD_ALERT` add column `OID_CANDIDACY_HASH_CODE` bigint(20);
alter table `PHD_ALERT` add index (`KEY_CANDIDACY_HASH_CODE`), add index (`OID_CANDIDACY_HASH_CODE`);

alter table `PHD_ALERT` change column `KEY_PROCESS` `KEY_PROCESS` int(11) DEFAULT NULL, change column `OID_PROCESS` `OID_PROCESS` bigint(20) DEFAULT NULL;
alter table `PHD_ALERT` change column `SEND_EMAIL` `SEND_EMAIL` tinyint(1) NOT NULL DEFAULT 0;
