update SUMMARY set SUMMARY.TITLE = '(Sem Titulo)' where SUMMARY.TITLE is null or SUMMARY.TITLE like '';

alter table SUMMARY change column TITLE OLD_TITLE text;
alter table SUMMARY change column SUMMARY_TEXT OLD_SUMMARY_TEXT text;

update SUMMARY set OLD_TITLE = '__xpto__' where OLD_TITLE is null or OLD_TITLE = '';
update SUMMARY set OLD_SUMMARY_TEXT = '__xpto__' where OLD_SUMMARY_TEXT is null or OLD_SUMMARY_TEXT = '';

alter table SUMMARY add column TITLE longtext;
alter table SUMMARY add column SUMMARY_TEXT longtext;

update SUMMARY set SUMMARY.TITLE = concat('pt', length(replace(SUMMARY.OLD_TITLE, "__xpto__", "")), ':', replace(SUMMARY.OLD_TITLE, "__xpto__", "")); 
update SUMMARY set SUMMARY.SUMMARY_TEXT = concat('pt', length(replace(SUMMARY.OLD_SUMMARY_TEXT, "__xpto__", "")), ':', replace(SUMMARY.OLD_SUMMARY_TEXT, "__xpto__", "")); 

update SUMMARY set TITLE = NULL WHERE TITLE = "pt0:en0:";
update SUMMARY set TITLE = replace(SUMMARY.TITLE, "pt0:", "");
update SUMMARY set TITLE = replace(SUMMARY.TITLE, "en0:", "");

update SUMMARY set SUMMARY_TEXT = NULL WHERE SUMMARY_TEXT = "pt0:en0:";
update SUMMARY set SUMMARY_TEXT = replace(SUMMARY.SUMMARY_TEXT, "pt0:", "");
update SUMMARY set SUMMARY_TEXT = replace(SUMMARY.SUMMARY_TEXT, "en0:", "");
