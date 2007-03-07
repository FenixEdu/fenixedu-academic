package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class IdDocument extends IdDocument_Base {
    
    public IdDocument() {
        super();
    }

    public static Collection<IdDocument> find(final String idDocumentValue) {
	final Collection<IdDocument> idDocuments = new ArrayList<IdDocument>();
	for (final IdDocument idDocument : RootDomainObject.getInstance().getIdDocumentsSet()) {
	    if (idDocument.getValue().equalsIgnoreCase(idDocumentValue)) {
		idDocuments.add(idDocument);
	    }
	}
	return idDocuments;
    }

}
