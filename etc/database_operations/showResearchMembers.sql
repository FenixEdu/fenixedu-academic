 alter table SITE add column SHOW_RESEARCH_MEMBERS tinyint(1);
 update SITE set SHOW_RESEARCH_MEMBERS=1 where OJB_CONCRETE_CLASS like '%ResearchUnitSite';

