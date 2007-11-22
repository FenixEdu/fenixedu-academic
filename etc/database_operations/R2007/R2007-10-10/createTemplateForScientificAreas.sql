insert into SITE(OJB_CONCRETE_CLASS, SITE_TYPE) values('net.sourceforge.fenixedu.domain.SiteTemplate','net.sourceforge.fenixedu.domain.ScientificAreaSite');

update SITE S, PARTY P set S.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.ScientificAreaSite' WHERE S.OJB_CONCRETE_CLASS like '%.UnitSite' AND S.KEY_TEMPLATE IS NULL and S.KEY_UNIT = P.ID_INTERNAL AND P.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit';

update SITE S, SITE S2  set S.KEY_TEMPLATE=S2.ID_INTERNAL WHERE S.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.ScientificAreaSite' AND S2.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.SiteTemplate' AND S2.SITE_TYPE='net.sourceforge.fenixedu.domain.ScientificAreaSite';

update SITE S, ACCESSIBLE_ITEM AI set S.KEY_MODULE=AI.ID_INTERNAL WHERE S.SITE_TYPE='net.sourceforge.fenixedu.domain.ScientificAreaSite' AND AI.UUID='da8c4e72-f6f9-43ac-82f9-61be1fb7d367';
