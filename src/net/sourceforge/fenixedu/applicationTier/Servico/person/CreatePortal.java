package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreatePortal extends Service {

    public void run(MetaDomainObject metaDomainObject, MultiLanguageString name,
	    String prefix) {

	Portal portal = new MetaDomainObjectPortal(metaDomainObject);
	configurePortal(portal, name, prefix);
    }

    public void run(Container container, MultiLanguageString name,
	    String prefix) {
	Portal portal = new Portal();
	portal.setFirstParent(container);
	configurePortal(portal, name, prefix);

    }

    private void configurePortal(Portal portal, MultiLanguageString name,
	String prefix) {
	portal.setName(name);
	portal.setPrefix(prefix);
    }
}
