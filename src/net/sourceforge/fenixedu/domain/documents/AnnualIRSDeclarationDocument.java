package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class AnnualIRSDeclarationDocument extends AnnualIRSDeclarationDocument_Base {

	public AnnualIRSDeclarationDocument(Person addressee, Person operator, String filename, byte[] content, Integer year) {
		super();
		checkParameters(year);
		checkRulesToCreate(addressee, year);
		setYear(year);
		super.init(GeneratedDocumentType.ANNUAL_IRS_DECLARATION, addressee, operator, filename, content);
	}

	@Override
	public Person getAddressee() {
		return (Person) super.getAddressee();
	}

	@Override
	protected Group computePermittedGroup() {
		return new RoleGroup(RoleType.MANAGER);
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

	@Service
	public AnnualIRSDeclarationDocument generateAnotherDeclaration(Person operator, byte[] content) {

		final Person addressee = getAddressee();
		final Integer year = getYear();

		delete();

		return new AnnualIRSDeclarationDocument(addressee, operator, buildFilename(addressee, year), content, year);
	}

	static private String buildFilename(Person person, Integer year) {
		return String.format("IRS-%s-%s-%s.pdf", year, person.getDocumentIdNumber(), new LocalDate().toString("yyyyMMdd"));
	}

	@Service
	static public AnnualIRSDeclarationDocument create(Person addressee, Person operator, byte[] content, Integer year) {
		return new AnnualIRSDeclarationDocument(addressee, operator, buildFilename(addressee, year), content, year);
	}
}
