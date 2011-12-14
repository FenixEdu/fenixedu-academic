package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers;

import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExistingEnrolmentsToValidate implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;
	return bean.getEnrolmentsPerformedByStudent();
    }
}
