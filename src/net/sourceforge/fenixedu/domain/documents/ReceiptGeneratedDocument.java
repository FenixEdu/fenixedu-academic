package net.sourceforge.fenixedu.domain.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

/**
 * @author Pedro Santos (pmrsa)
 */
public class ReceiptGeneratedDocument extends ReceiptGeneratedDocument_Base {
    public ReceiptGeneratedDocument(Receipt source, Party addressee, Person operator, String filename, InputStream stream) {
	super();
	setSource(source);
	init(GeneratedDocumentType.RECEIPT, addressee, operator, filename, stream);
    }
}
