alter table ACCOUNTABILITY_TYPE add column TYPE_NAME longtext;

update ACCOUNTABILITY_TYPE set NAME = '__xpto__' where NAME is null or NAME like '';
update ACCOUNTABILITY_TYPE set ACCOUNTABILITY_TYPE.TYPE_NAME = concat('pt', length(replace(ACCOUNTABILITY_TYPE.NAME, "__xpto__", "")), ':', replace(ACCOUNTABILITY_TYPE.NAME, "__xpto__", ""));

update ACCOUNTABILITY_TYPE set TYPE_NAME = NULL WHERE TYPE_NAME = "pt0:en0:";
update ACCOUNTABILITY_TYPE set TYPE_NAME = replace(ACCOUNTABILITY_TYPE.TYPE_NAME, "pt0:", "");
update ACCOUNTABILITY_TYPE set TYPE_NAME = replace(ACCOUNTABILITY_TYPE.TYPE_NAME, "en0:", "");
