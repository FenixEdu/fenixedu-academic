alter table RESEARCH_INTEREST drop column INTEREST;
alter table RESEARCH_INTEREST add column INTEREST longtext NOT NULL default '';