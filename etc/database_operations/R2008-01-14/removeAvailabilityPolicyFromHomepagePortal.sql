
update CONTENT set KEY_AVAILABILITY_POLICY = NULL WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND KEY_META_DOMAIN_OBJECT IN (SELECT ID_INTERNAL FROM META_DOMAIN_OBJECT WHERE TYPE='net.sourceforge.fenixedu.domain.homepage.Homepage');

delete FROM AVAILABILITY_POLICY WHERE ID_INTERNAL = (SELECT ID_INTERNAL FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND KEY_META_DOMAIN_OBJECT IN (SELECT ID_INTERNAL FROM META_DOMAIN_OBJECT WHERE TYPE='net.sourceforge.fenixedu.domain.homepage.Homepage'))
