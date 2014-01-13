package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Portal;
import pt.ist.fenixframework.Atomic;

public class AddPortalToContainer {

    @Atomic
    public static void run(Container container, Portal portal) {
        container.addChild(portal);
    }
}