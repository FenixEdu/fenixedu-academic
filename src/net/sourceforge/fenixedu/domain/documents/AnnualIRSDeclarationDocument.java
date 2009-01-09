package net.sourceforge.fenixedu.domain.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AnnualIRSDeclarationDocument extends AnnualIRSDeclarationDocument_Base {

    protected AnnualIRSDeclarationDocument() {
	super();
    }

    public AnnualIRSDeclarationDocument(Person addressee, Person operator, String filename, InputStream stream, Integer year) {
	super();
	init(GeneratedDocumentType.ANNUAL_IRS_DECLARATION, addressee, operator, filename, stream, year);
    }

    private void init(GeneratedDocumentType type, Person addressee, Person operator, String filename, InputStream stream,
	    Integer year) {

	checkParameters(year);

	checkRulesToCreate(addressee, year);

	super.init(type, addressee, operator, filename, stream);

	super.setYear(year);

    }

    private void checkRulesToCreate(Person addressee, Integer year) {
	if (addressee.hasAnnualIRSDocumentFor(year)) {
	    throw new DomainException("error.documents.AnnualIRSDeclarationDocument.annual.irs.document.alread.exists.for.year");
	}

    }

    private void checkParameters(Integer year) {
	if (year == null) {
	    throw new DomainException("error.documents.AnnualIRSDeclarationDocument.year.cannot.be.null");
	}

    }

}
