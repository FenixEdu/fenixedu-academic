alter table LESSON_PLANNING change column TITLE OLD_TITLE text;
alter table LESSON_PLANNING change column PLANNING OLD_PLANNING text;

update LESSON_PLANNING set OLD_TITLE = '__xpto__' where OLD_TITLE is null or OLD_TITLE = '';
update LESSON_PLANNING set OLD_PLANNING = '__xpto__' where OLD_PLANNING is null or OLD_PLANNING = '';

alter table LESSON_PLANNING add column TITLE longtext;
alter table LESSON_PLANNING add column PLANNING longtext;

update LESSON_PLANNING set LESSON_PLANNING.TITLE = concat('pt', length(replace(LESSON_PLANNING.OLD_TITLE, "__xpto__", "")), ':', replace(LESSON_PLANNING.OLD_TITLE, "__xpto__", "")); 
update LESSON_PLANNING set LESSON_PLANNING.PLANNING = concat('pt', length(replace(LESSON_PLANNING.OLD_PLANNING, "__xpto__", "")), ':', replace(LESSON_PLANNING.OLD_PLANNING, "__xpto__", "")); 

update LESSON_PLANNING set TITLE = NULL WHERE TITLE = "pt0:en0:";
update LESSON_PLANNING set TITLE = replace(LESSON_PLANNING.TITLE, "pt0:", "");
update LESSON_PLANNING set TITLE = replace(LESSON_PLANNING.TITLE, "en0:", "");

update LESSON_PLANNING set PLANNING = NULL WHERE PLANNING = "pt0:en0:";
update LESSON_PLANNING set PLANNING = replace(LESSON_PLANNING.PLANNING, "pt0:", "");
update LESSON_PLANNING set PLANNING = replace(LESSON_PLANNING.PLANNING, "en0:", "");
