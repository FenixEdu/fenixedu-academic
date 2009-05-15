alter table `FILE` add column `DOCUMENT_TYPE` varchar(255) null;
alter table `FILE` add column `KEY_PHD_CANDIDACY_PROCESS` int(11) null, add column `OID_PHD_CANDIDACY_PROCESS` bigint unsigned default null;
alter table `FILE` add index (`KEY_PHD_CANDIDACY_PROCESS`), add index (`OID_PHD_CANDIDACY_PROCESS`);
