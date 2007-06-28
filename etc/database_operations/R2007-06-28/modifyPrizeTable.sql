alter table RESEARCH_PRIZE change column NAME NAME longtext;
update RESEARCH_PRIZE set NAME=concat('pt',length(NAME),':',NAME);
