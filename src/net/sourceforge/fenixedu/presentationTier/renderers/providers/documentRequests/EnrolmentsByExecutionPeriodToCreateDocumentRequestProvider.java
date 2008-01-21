package net.sourceforge.fenixedu.presentationTier.renderers.providers.documentRequests;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EnrolmentsByExecutionPeriodToCreateDocumentRequestProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

	if (documentRequestCreateBean.getExecutionPeriod() != null) {
	    return documentRequestCreateBean.getRegistration().getLastStudentCurricularPlan().getEnrolmentsByExecutionPeriod(
		    documentRequestCreateBean.getExecutionPeriod());
	}

	return Collections.EMPTY_LIST;

    }
}
