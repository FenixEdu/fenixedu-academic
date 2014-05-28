package net.sourceforge.fenixedu.presentationTier.renderers.providers.library;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LibraryProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        return Bennu.getInstance().getLibrariesSet().stream().filter(s -> s.isActive()).collect(Collectors.toSet());
    }

}
