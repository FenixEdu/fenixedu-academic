package net.sourceforge.fenixedu.presentationTier.renderers.providers.documentRequests;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EnrolmentsByExecutionPeriodToCreateDocumentRequestProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

        if (documentRequestCreateBean.getExecutionPeriod() != null) {
            return documentRequestCreateBean.getRegistration().getLastStudentCurricularPlan()
                    .getEnrolmentsByExecutionPeriod(documentRequestCreateBean.getExecutionPeriod());
        }

        return Collections.EMPTY_LIST;

    }
}
