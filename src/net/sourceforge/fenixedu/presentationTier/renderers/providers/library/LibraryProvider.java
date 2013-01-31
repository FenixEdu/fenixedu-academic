package net.sourceforge.fenixedu.presentationTier.renderers.providers.library;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LibraryProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(Object arg0, Object arg1) {
		RootDomainObject root = RootDomainObject.getInstance();
		return root.getLibraries();
	}

}
