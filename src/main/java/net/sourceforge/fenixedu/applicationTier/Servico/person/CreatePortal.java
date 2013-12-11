package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreatePortal {

    @Atomic
    public static void run(String type, MultiLanguageString name, String prefix) {
        Portal portal = new MetaDomainObjectPortal(type);
        configurePortal(portal, name, prefix);
    }

    @Atomic
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