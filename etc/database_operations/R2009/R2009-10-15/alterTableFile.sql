alter table FILE change column OID_PHD_CANDIDACY_PROCESS OID_PHD_PROGRAM_PROCESS bigint(20) unsigned null;
alter table FILE change column KEY_PHD_CANDIDACY_PROCESS KEY_PHD_PROGRAM_PROCESS int(11) null;

alter table FILE add column DOCUMENT_VERSION int(11) null;

