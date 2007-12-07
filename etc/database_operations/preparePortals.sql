alter table CONTENT add column KEY_META_DOMAIN_OBJECT int(11);

INSERT INTO CONTENT (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, CREATION_DATE, PREFIX)
VALUES ('net.sourceforge.fenixedu.domain.Section', 1, 'pt7:Privadoen7:Private', NOW(), '/private');


INSERT INTO NODE (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, KEY_PARENT, KEY_CHILD, NODE_ORDER)
SELECT 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', 1, P.ID_INTERNAL, C.ID_INTERNAL, 0
FROM CONTENT P, CONTENT C
WHERE P.OJB_CONCRETE_CLASS LIKE '%Portal'
  AND C.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.Section'
  AND C.NAME = 'pt7:Privadoen7:Private';


