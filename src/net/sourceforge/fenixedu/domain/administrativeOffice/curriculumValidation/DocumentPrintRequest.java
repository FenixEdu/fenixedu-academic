package net.sourceforge.fenixedu.domain.administrativeOffice.curriculumValidation;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class DocumentPrintRequest extends DocumentPrintRequest_Base {

	public DocumentPrintRequest() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public DocumentPrintRequest(String conclusionDateValue, String degreeDescriptionValue, String graduatedTitleValue,
			DocumentRequest request) {
		this();

		if (AccessControl.getPerson() == null) {
			throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.person.cannot.be.null");
		}

		if (request == null) {
			throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.person.cannot.be.null");
		}

		super.setWhoRequested(AccessControl.getPerson());
		super.setDocumentRequest(request);
		super.setNewConclusionDateValue(conclusionDateValue);
		super.setNewDegreeDescriptionValue(degreeDescriptionValue);
		super.setWhenRequested(new DateTime());
		super.setNewGraduatedTitleValue(graduatedTitleValue);
	}

	@Service
	public static DocumentPrintRequest logRequest(String conclusionDateValue, String degreeDescriptionValue,
			String graduatedTitleValue, DocumentRequest request) {
		return new DocumentPrintRequest(conclusionDateValue, degreeDescriptionValue, graduatedTitleValue, request);
	}

	@Override
	public void setDocumentRequest(DocumentRequest documentRequest) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

	@Override
	public void setNewConclusionDateValue(String newConclusionDateValue) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

	@Override
	public void setNewDegreeDescriptionValue(String newDegreeDescriptionValue) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

	@Override
	public void setNewGraduatedTitleValue(String newGraduatedTitleValue) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

	@Override
	public void setWhenRequested(DateTime when) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

	@Override
	public void setWhoRequested(Person whoRequested) {
		throw new DomainException("administrativeOffice.curriculumValidation.DocumentPrintRequest.cannot.modify.value");
	}

}
