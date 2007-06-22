alter table RESEARCH_EVENT add column STAGE varchar(255) not NULL default 'DRAFT';
alter table SCIENTIFIC_JOURNAL add column STAGE varchar(255) not NULL default 'DRAFT';