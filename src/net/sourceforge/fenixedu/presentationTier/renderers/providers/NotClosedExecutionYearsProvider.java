package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class NotClosedExecutionYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ExecutionYear.readNotClosedExecutionYears();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
