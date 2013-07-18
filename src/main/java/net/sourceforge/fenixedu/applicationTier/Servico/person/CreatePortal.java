package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreatePortal {

    @Service
    public static void run(MetaDomainObject metaDomainObject, MultiLanguageString name, String prefix) {

        Portal portal = new MetaDomainObjectPortal(metaDomainObject);
        configurePortal(portal, name, prefix);
    }

    @Service
    public static void run(Container container, MultiLanguageString name, String prefix) {
        Portal portal = new Portal();
        portal.setFirstParent(container);
        configurePortal(portal, name, prefix);

    }

    private static void configurePortal(Portal portal, MultiLanguageString name, String prefix) {
        portal.setName(name);
        portal.setPrefix(prefix);
    }
}