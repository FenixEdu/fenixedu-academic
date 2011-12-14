package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AccountabilityTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return new ArrayList(RootDomainObject.getInstance().getAccountabilityTypes());
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
