package net.sourceforge.fenixedu.domain.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentRequestGeneratedDocument extends DocumentRequestGeneratedDocument_Base {
    public DocumentRequestGeneratedDocument(DocumentRequest source, Party addressee, Person operator, String filename,
	    InputStream stream) {
	super();
	setSource(source);
	init(GeneratedDocumentType.determineType(source.getDocumentRequestType()), addressee, operator, filename, stream);
    }
}
