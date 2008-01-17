package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EnrolmentsToCreateDocumentRequestProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

	if (documentRequestCreateBean.getExecutionYear() != null) {
	    return documentRequestCreateBean.getRegistration().getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(
		    documentRequestCreateBean.getExecutionYear());
	}

	return Collections.EMPTY_LIST;

    }

}
