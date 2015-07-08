package org.fenixedu.academic.ui.renderers.providers;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DocumentPurposeTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return DocumentPurposeTypeInstance.findActives().sorted(DocumentPurposeTypeInstance.COMPARE_BY_LEGACY)
                .collect(Collectors.toSet());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
