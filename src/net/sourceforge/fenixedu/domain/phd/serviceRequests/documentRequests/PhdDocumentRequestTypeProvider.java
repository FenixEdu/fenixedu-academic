package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdDocumentRequestTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new EnumConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(new DocumentRequestType[] { DocumentRequestType.DIPLOMA_REQUEST,
		DocumentRequestType.REGISTRY_DIPLOMA_REQUEST, DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST });
    }

}
