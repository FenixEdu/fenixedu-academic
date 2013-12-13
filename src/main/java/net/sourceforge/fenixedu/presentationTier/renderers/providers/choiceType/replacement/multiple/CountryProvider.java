package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountryProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Bennu.getInstance().getCountrysSet();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
