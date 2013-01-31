package net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class AcademicServiceRequestTypeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Arrays.asList(AcademicServiceRequestType.values());
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

}
