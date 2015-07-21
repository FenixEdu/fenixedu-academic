package org.fenixedu.academic.ui.renderers.providers;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import edu.emory.mathcs.backport.java.util.Collections;

public class DocumentSignerProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<DocumentSigner> documentSignatures =
                DocumentSigner.findAll()
                        .filter(ds -> ds.getAdministrativeOffice() == AdministrativeOffice.readDegreeAdministrativeOffice())
                        .sorted(DocumentSigner.DEFAULT_COMPARATOR).collect(Collectors.toList());
        Collections.sort(documentSignatures, DocumentSigner.DEFAULT_COMPARATOR);
        return documentSignatures;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
