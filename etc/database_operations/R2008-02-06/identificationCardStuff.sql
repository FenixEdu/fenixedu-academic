


-- Inserted at 2007-12-04T19:25:48.682Z

alter table CARD_GENERATION_PROBLEM add column KEY_PERSON int(11);
alter table CARD_GENERATION_PROBLEM add index (KEY_PERSON);





-- Inserted at 2007-12-13T18:31:10.559Z

alter table CARD_GENERATION_BATCH add column DESCRIPTION text;





-- Inserted at 2007-12-14T14:33:09.595Z

alter table CARD_GENERATION_ENTRY add column DOCUMENT_I_D text;


insert into ROLE (ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY, KEY_ROOT_DOMAIN_OBJECT, OJB_CONCRETE_CLASS)
    values ("IDENTIFICATION_CARD_MANAGER", "/identificationCardManager", "/index.do", "portal.identificationCardManager", 1, "net.sourceforge.fenixedu.domain.Role");


