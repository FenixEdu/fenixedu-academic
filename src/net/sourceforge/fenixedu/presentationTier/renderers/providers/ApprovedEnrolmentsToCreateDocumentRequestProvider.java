package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ApprovedEnrolmentsToCreateDocumentRequestProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

	final AdministrativeOffice administrativeOffice = AccessControl.getPerson().getEmployeeAdministrativeOffice();
	final List<Enrolment> result = documentRequestCreateBean.getStudent().getApprovedEnrolments(administrativeOffice);
	Collections.sort(result, Enrolment.COMPARATOR_BY_NAME_AND_ID);

	return result;
    }

}
