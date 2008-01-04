insert into META_DOMAIN_OBJECT(TYPE) SELECT SITE_TYPE FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.SiteTemplate';

update CONTENT set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.SiteTemplate';

alter table CONTENT add index (OJB_CONCRETE_CLASS);

alter table META_DOMAIN_OBJECT add index (TYPE);

update CONTENT C, META_DOMAIN_OBJECT MDO set C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.SITE_TYPE=MDO.TYPE;

alter table CONTENT drop column SITE_TYPE;

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='2553ca76-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ExecutionCourseSite';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='2574818a-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.homepage.Homepage';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='2574881a-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.DegreeSite';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='2586b648-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ResearchUnitSite';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='259fbb84-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.PedagogicalCouncilSite';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='259fb95e-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ScientificCouncilSite';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.CONTENT_ID='25abc654-f86e-102a-aeb6-0013d3b09da0' where C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ScientificAreaSite';
