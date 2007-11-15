package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EnrolmentsForRegistrationAcademicServiceRequestsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ((RegistrationSelectExecutionYearBean) source).getRegistration().getLastStudentCurricularPlan()
		.getStudentEnrollmentsWithEnrolledState();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
