select concat('insert into RESOURCE_RESPONSIBILITY (OJB_CONCRETE_CLASS, KEY_RESOURCE, KEY_PARTY, BEGIN, END) values (\"net.sourceforge.fenixedu.domain.material.MaterialResponsibility\",' , ID_INTERNAL , ',' , KEY_OWNER , ',\"' , ACQUISITION , '\",\"' , COALESCE(CEASE,'') , '\");') as "" from RESOURCE where KEY_OWNER is not null;

alter table RESOURCE drop column KEY_OWNER;

delete from OJB_HL_SEQ;