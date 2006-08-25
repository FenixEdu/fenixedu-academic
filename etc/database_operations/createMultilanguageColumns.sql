alter table SECTION drop index U1;
alter table SECTION change column NAME OLD_NAME text;
update SECTION set OLD_NAME = '__xpto__' where OLD_NAME is null or OLD_NAME = '';
alter table SECTION add column NAME longtext;
update SECTION set SECTION.NAME = concat('pt', length(replace(SECTION.OLD_NAME, "__xpto__", "")), ':', replace(SECTION.OLD_NAME, "__xpto__", ""));
update SECTION set NAME = NULL WHERE NAME = "pt0:";
update SECTION set NAME = replace(SECTION.NAME, "pt0:", "");

alter table ITEM drop index U1;
alter table ITEM change column NAME OLD_NAME text;
update ITEM set OLD_NAME = '__xpto__' where OLD_NAME is null or OLD_NAME = '';
alter table ITEM add column NAME longtext;
update ITEM set ITEM.NAME = concat('pt', length(replace(ITEM.OLD_NAME, "__xpto__", "")), ':', replace(ITEM.OLD_NAME, "__xpto__", ""));
update ITEM set NAME = NULL WHERE NAME = "pt0:";
update ITEM set NAME = replace(ITEM.NAME, "pt0:", "");

alter table ITEM change column INFORMATION OLD_INFORMATION text;
update ITEM set OLD_INFORMATION = '__xpto__' where OLD_INFORMATION is null or OLD_INFORMATION = '';
alter table ITEM add column INFORMATION longtext;
update ITEM set ITEM.INFORMATION = concat('pt', length(replace(ITEM.OLD_INFORMATION, "__xpto__", "")), ':', replace(ITEM.OLD_INFORMATION, "__xpto__", ""));
update ITEM set INFORMATION = NULL WHERE INFORMATION = "pt0:";
update ITEM set INFORMATION = replace(ITEM.INFORMATION, "pt0:", "");
