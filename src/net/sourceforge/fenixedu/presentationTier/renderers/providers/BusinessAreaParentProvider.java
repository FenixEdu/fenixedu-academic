package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.BusinessArea;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BusinessAreaParentProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return BusinessArea.getParentBusinessAreas();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
