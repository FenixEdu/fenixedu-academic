package net.sourceforge.fenixedu.presentationTier.renderers.providers.contents;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MetaDomainObjectPortalProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		List<MetaDomainObjectPortal> portals = new ArrayList<MetaDomainObjectPortal>();

		for (MetaDomainObject metaDomainObject : RootDomainObject.getInstance().getMetaDomainObjects()) {
			if (metaDomainObject.isPortalAvailable()) {
				portals.add((MetaDomainObjectPortal) metaDomainObject.getAssociatedPortal());
			}
		}
		return portals;
	}

}
