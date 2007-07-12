DELETE FROM A USING ACCESSIBLE_ITEM AS A, SITE AS S
WHERE A.KEY_SITE = S.ID_INTERNAL
  AND S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.SiteTemplate'
  AND S.SITE_TYPE IN (
  	'net.sourceforge.fenixedu.domain.DepartmentSite', 
  	'net.sourceforge.fenixedu.domain.ResearchUnitSite'
  );
