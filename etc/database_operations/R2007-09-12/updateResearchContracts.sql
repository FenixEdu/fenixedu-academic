update ACCOUNTABILITY set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.organizationalStructure.ResearcherContract' where KEY_ACCOUNTABILITY_TYPE=1380 OR KEY_ACCOUNTABILITY_TYPE= 1381;

update ACCOUNTABILITY set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.organizationalStructure.ResearchScholarshipContract' where KEY_ACCOUNTABILITY_TYPE=1385 OR KEY_ACCOUNTABILITY_TYPE=1386;

update ACCOUNTABILITY set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.organizationalStructure.ResearchTechnicalStaffContract' where KEY_ACCOUNTABILITY_TYPE=1384 ;

update ACCOUNTABILITY set KEY_ACCOUNTABILITY_TYPE = (select ID_INTERNAL FROM ACCOUNTABILITY_TYPE WHERE TYPE='RESEARCH_CONTRACT') WHERE OJB_CONCRETE_CLASS like '%Research%';