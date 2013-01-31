package net.sourceforge.fenixedu.presentationTier.renderers.providers.documentRequests;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsToCreateDocumentRequestProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

		if (documentRequestCreateBean.getExecutionYear() != null) {
			return documentRequestCreateBean.getExecutionYear().getExecutionPeriods();
		}

		return Collections.EMPTY_LIST;
	}

}
