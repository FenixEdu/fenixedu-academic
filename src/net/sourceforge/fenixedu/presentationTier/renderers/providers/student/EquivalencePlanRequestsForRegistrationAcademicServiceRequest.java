package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EquivalencePlanRequestsForRegistrationAcademicServiceRequest implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	return bean.getRegistration().getStudent().getAcademicServiceRequests(EquivalencePlanRequest.class);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
