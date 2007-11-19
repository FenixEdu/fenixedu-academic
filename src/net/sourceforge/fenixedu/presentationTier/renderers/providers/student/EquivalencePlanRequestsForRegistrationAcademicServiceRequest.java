package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EquivalencePlanRequestsForRegistrationAcademicServiceRequest implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	return bean.getRegistration().getAcademicServiceRequests(EquivalencePlanRequest.class);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
