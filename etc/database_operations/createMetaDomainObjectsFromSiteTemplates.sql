insert into META_DOMAIN_OBJECT(TYPE) SELECT SITE_TYPE FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.SiteTemplate';

update CONTENT set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.SiteTemplate';

update CONTENT C, META_DOMAIN_OBJECT MDO set C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.SITE_TYPE=MDO.TYPE;

alter table CONTENT drop column SITE_TYPE;
