package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Portal;

public class AddPortalToContainer extends Service {

    public void run(Container container, Portal portal) {
	container.addChild(portal);
    }
}
