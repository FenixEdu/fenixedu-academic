package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StandaloneApprovedEnrolmentsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;
	return documentRequestCreateBean.getRegistration().getLastStudentCurricularPlan()
		.getStandaloneApprovedEnrolmentsNotInDismissal();
    }
}
