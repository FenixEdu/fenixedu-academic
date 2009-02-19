package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AnnualIRSDeclarationDocument extends AnnualIRSDeclarationDocument_Base {

    public AnnualIRSDeclarationDocument(Person addressee, Person operator, String filename, byte[] content, Integer year) {
	super();
	checkParameters(year);
	checkRulesToCreate(addressee, year);
	setYear(year);
	super.init(GeneratedDocumentType.ANNUAL_IRS_DECLARATION, addressee, operator, filename, content);
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
