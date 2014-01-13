package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AnnouncementCategoryProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return Bennu.getInstance().getCategoriesSet();
    }

}
