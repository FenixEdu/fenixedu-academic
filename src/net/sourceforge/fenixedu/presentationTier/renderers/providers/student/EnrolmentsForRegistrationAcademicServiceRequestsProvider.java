package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EnrolmentsForRegistrationAcademicServiceRequestsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	final List<Enrolment> enrolments = new ArrayList<Enrolment>(bean.getRegistration().getLastStudentCurricularPlan()
		.getStudentEnrollmentsWithEnrolledState());
	Collections.sort(enrolments, Enrolment.COMPARATOR_BY_NAME_AND_ID);
	return enrolments;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
