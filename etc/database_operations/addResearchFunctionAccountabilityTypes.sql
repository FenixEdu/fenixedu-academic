alter table ACCOUNTABILITY_TYPE add column RESEARCH_FUNCTION_TYPE varchar(80) ;

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'PERMANENT_RESEARCHER');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'INVITED_RESEARCHER');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'TECHNICAL_STAFF');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'COLLABORATORS');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'OTHER_STAFF');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'PHD_STUDENT');

insert into ACCOUNTABILITY_TYPE(TYPE,OJB_CONCRETE_CLASS,RESEARCH_FUNCTION_TYPE) values('RESEARCH_CONTRACT','net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunction', 'MSC_STUDENT');
