package org.fenixedu.academic.ui.renderers.providers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DocumentPurposeTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<DocumentPurposeTypeInstance> documentPurposeTypes =
                DocumentPurposeTypeInstance.findActives().sorted(DocumentPurposeTypeInstance.COMPARE_BY_LEGACY)
                        .collect(Collectors.toList());
        // DSimoes: For some weird reason the pipeline sorter was not working right... :/
        Collections.sort(documentPurposeTypes, DocumentPurposeTypeInstance.COMPARE_BY_LEGACY);
        return documentPurposeTypes;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
