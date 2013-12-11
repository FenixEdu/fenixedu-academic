package net.sourceforge.fenixedu.domain.administrativeOffice.curriculumValidation;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class DocumentPrintRequest extends DocumentPrintRequest_Base {

    public DocumentPrintRequest() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Atomic
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

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNewGraduatedTitleValue() {
        return getNewGraduatedTitleValue() != null;
    }

    @Deprecated
    public boolean hasDocumentRequest() {
        return getDocumentRequest() != null;
    }

    @Deprecated
    public boolean hasNewConclusionDateValue() {
        return getNewConclusionDateValue() != null;
    }

    @Deprecated
    public boolean hasNewDegreeDescriptionValue() {
        return getNewDegreeDescriptionValue() != null;
    }

    @Deprecated
    public boolean hasWhoRequested() {
        return getWhoRequested() != null;
    }

    @Deprecated
    public boolean hasWhenRequested() {
        return getWhenRequested() != null;
    }

}
