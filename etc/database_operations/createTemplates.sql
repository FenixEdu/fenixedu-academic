INSERT INTO SITE (ROOT_DOMAIN_OBJECT, OJB_CONCRETE_CLASS, SITE_TYPE, KEY_MODULE)
SELECT
	1,
	'net.sourceforge.fenixedu.domain.SiteTemplate',
	'net.sourceforge.fenixedu.domain.PedagogicalCouncilSite',
	F.ID_INTERNAL
FROM ACCESSIBLE_ITEM F
WHERE F.UUID = 'd569fa9c-e701-4891-be2d-6c3463db0312';

INSERT INTO SITE (ROOT_DOMAIN_OBJECT, OJB_CONCRETE_CLASS, SITE_TYPE, KEY_MODULE)
SELECT
	1,
	'net.sourceforge.fenixedu.domain.SiteTemplate',
	'net.sourceforge.fenixedu.domain.ScientificCouncilSite',
	F.ID_INTERNAL
FROM ACCESSIBLE_ITEM F
WHERE F.UUID = '07749b24-d2fd-43d2-8254-67dae4aa8492';

UPDATE SITE S, SITE T
SET S.KEY_TEMPLATE = T.ID_INTERNAL
WHERE S.OJB_CONCRETE_CLASS IN (
			'net.sourceforge.fenixedu.domain.PedagogicalCouncilSite', 
			'net.sourceforge.fenixedu.domain.ScientificCouncilSite')
  AND T.SITE_TYPE = S.OJB_CONCRETE_CLASS;
