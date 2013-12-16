package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.ArrayList;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AccountabilityTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return new ArrayList(Bennu.getInstance().getAccountabilityTypesSet());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
