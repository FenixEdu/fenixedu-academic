package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdAcademicServiceRequestTypeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Collections.singletonList(AcademicServiceRequestType.PHD_STUDENT_REINGRESSION);
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

}
