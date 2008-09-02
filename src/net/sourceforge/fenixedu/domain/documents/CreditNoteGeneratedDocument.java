package net.sourceforge.fenixedu.domain.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CreditNoteGeneratedDocument extends CreditNoteGeneratedDocument_Base {
    public CreditNoteGeneratedDocument(CreditNote source, Party addressee, Person operator, String filename, InputStream stream) {
	super();
	setSource(source);
	init(GeneratedDocumentType.CREDIT_NOTE, addressee, operator, filename, stream);
    }
}
