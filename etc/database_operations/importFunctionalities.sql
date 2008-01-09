-- importar funcionalidades Público/*
-- ligar Público ao RootModule

-- importar FunctionalitySection, ou seja, criar node que liga pai da FunctionalitySection à Functionality

insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,ASCENDING,OJB_CONCRETE_CLASS) 
select C2.ID_INTERNAL, C.ID_INTERNAL,AI1.SECTION_ORDER,'1','net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' 
FROM ACCESSIBLE_ITEM AI1, ACCESSIBLE_ITEM AI2, CONTENT C, CONTENT C2 
WHERE AI1.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.FunctionalitySection' AND AI1.KEY_FUNCTIONALITY=AI2.ID_INTERNAL 
AND C.CONTENT_ID=AI2.UUID AND AI2.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality' 
AND C2.OLD_ID_INTERNAL=AI1.KEY_SITE AND AI1.KEY_SUPERIOR_SECTION IS NULL;

insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,ASCENDING,OJB_CONCRETE_CLASS) 
select C2.ID_INTERNAL, C.ID_INTERNAL,AI1.SECTION_ORDER,'1','net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' 
FROM ACCESSIBLE_ITEM AI1, ACCESSIBLE_ITEM AI2, CONTENT C, CONTENT C2 
WHERE AI1.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.FunctionalitySection' AND AI1.KEY_FUNCTIONALITY=AI2.ID_INTERNAL 
AND C.CONTENT_ID=AI2.UUID AND AI2.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality' 
AND AI1.KEY_SUPERIOR_SECTION IS NOT NULL AND AI1.KEY_SUPERIOR_SECTION=C2.OLD_ID_INTERNAL;


-- alterar SiteTemplate KEY_MODULE -> KEY_CONTAINER

-- Criar secção e ligar a SiteTemplates que tenho KEY_MODULE != NULL

-- Criar nós entre essa secção e todas as funcionalidades abaixo do módulo a que o 
-- SiteTemplate estava ligado


-- dump das tabelas AVAIBILITY_POLICY ROOT_DOMAIN_OBJECT 










-- copy funcionalities
INSERT INTO CONTENT (CONTENT_ID, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, CREATION_DATE)
SELECT UUID, 'net.sourceforge.fenixedu.domain.functionalities.Functionality', 1, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, NOW()
FROM ACCESSIBLE_ITEM WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality';

-- copy modules
INSERT INTO CONTENT (CONTENT_ID, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, PREFIX, CREATION_DATE)
SELECT UUID, 'net.sourceforge.fenixedu.domain.functionalities.Module', 1, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, PREFIX, NOW()
FROM ACCESSIBLE_ITEM WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module';

-- create nodes
INSERT INTO NODE (OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, VISIBLE)
SELECT 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', C_P.ID_INTERNAL, C_C.ID_INTERNAL, AI_C.ORDER_IN_MODULE, AI_C.VISIBLE
FROM CONTENT AS C_P, CONTENT AS C_C, ACCESSIBLE_ITEM AS AI_P, ACCESSIBLE_ITEM AS AI_C
WHERE AI_P.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module'
  AND (AI_C.KEY_PARENT = AI_P.ID_INTERNAL OR AI_C.KEY_MODULE = AI_P.ID_INTERNAL)
  AND C_P.CONTENT_ID = AI_P.UUID
  AND C_C.CONTENT_ID = AI_C.UUID;

-- create new functionalities
INSERT INTO CONTENT (CONTENT_ID, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, CREATION_DATE)
SELECT UUID, 'net.sourceforge.fenixedu.domain.functionalities.Functionality', 1, KEY_AVAILABILITY_POLICY, NAME, TITLE, DESCRIPTION, PATH, ENABLED, NOW()
FROM ACCESSIBLE_ITEM 
WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module'
  AND PRINCIPAL = 1
  AND PATH IS NOT NULL;

-- delete data from modules transformed into functionalities
UPDATE CONTENT AS C, ACCESSIBLE_ITEM AS AI
SET C.KEY_AVAILABILITY_POLICY = NULL, C.PATH = NULL
WHERE C.CONTENT_ID = AI.UUID
  AND C.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module'
  AND AI.PRINCIPAL = 1
  AND AI.PATH IS NOT NULL;

-- adjust order of nodes to compensate for new functionalities
UPDATE NODE AS N, CONTENT AS C, ACCESSIBLE_ITEM AS AI
SET N.NODE_ORDER = N.NODE_ORDER + 1
WHERE N.KEY_PARENT = C.ID_INTERNAL
  AND C.CONTENT_ID = AI.UUID
  AND C.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module'
  AND AI.PRINCIPAL = 1
  AND AI.PATH IS NOT NULL;

-- create nodes for new functionalities
INSERT INTO NODE (OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, VISIBLE)
SELECT 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', C_P.ID_INTERNAL, C_C.ID_INTERNAL, 0, AI_C.VISIBLE
FROM CONTENT AS C_P, CONTENT AS C_C, ACCESSIBLE_ITEM AS AI_C
WHERE C_P.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module'
  AND C_C.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Functionality'
  AND C_P.CONTENT_ID = C_C.CONTENT_ID
  AND AI_C.UUID = C_C.CONTENT_ID
  AND AI_C.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module';





-- update availability policy
UPDATE CONTENT AS C, AVAILABILITY_POLICY AS AP
SET AP.KEY_CONTENT = C.ID_INTERNAL
WHERE C.KEY_AVAILABILITY_POLICY = AP.ID_INTERNAL;

ALTER TABLE AVAILABILITY_POLICY
	MODIFY COLUMN KEY_ACCESSIBLE_ITEM INTEGER;
	
UPDATE AVAILABILITY_POLICY
SET KEY_ACCESSIBLE_ITEM = NULL 
WHERE KEY_CONTENT IS NOT NULL;
