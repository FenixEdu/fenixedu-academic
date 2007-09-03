alter table PARTY add column PARTY_NAME longtext;

update PARTY set NAME = '__xpto__' where NAME is null or NAME like '';
update PARTY set PARTY.PARTY_NAME = concat('pt', length(replace(PARTY.NAME, "__xpto__", "")), ':', replace(PARTY.NAME, "__xpto__", ""));
update PARTY set PARTY_NAME = replace(PARTY.PARTY_NAME, "pt0:", "pt4:????");

update PARTY set NAME_EN = '__xpto__' where NAME_EN is null or NAME like '';
update PARTY set PARTY.PARTY_NAME = concat(PARTY.PARTY_NAME, 'en', length(replace(PARTY.NAME_EN, "__xpto__", "")), ':', replace(PARTY.NAME_EN, "__xpto__", ""));
update PARTY set PARTY_NAME = replace(PARTY.PARTY_NAME, "en0:", "");

alter table ACCOUNTABILITY_TYPE drop column NAME;
alter table PARTY drop column NAME;
alter table PARTY drop column NAME_EN;