package net.sourceforge.fenixedu.presentationTier.renderers.providers.library;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LibraryProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        Bennu root = Bennu.getInstance();
        return root.getLibrariesSet();
    }

}
