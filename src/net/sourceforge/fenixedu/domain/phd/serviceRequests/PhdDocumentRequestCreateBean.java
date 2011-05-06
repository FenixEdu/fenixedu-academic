package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class PhdDocumentRequestCreateBean extends PhdAcademicServiceRequestCreateBean {
    private static final long serialVersionUID = 1L;

    private String givenNames;
    private String familyNames;

    private DocumentRequestType documentRequestType;

    public PhdDocumentRequestCreateBean(PhdIndividualProgramProcess phdIndividualProgramProcess) {
	super(phdIndividualProgramProcess);
	setRequestType(AcademicServiceRequestType.DOCUMENT);
	setGivenAndFamilyNames();
    }

    private void setGivenAndFamilyNames() {
	final Person person = getPhdIndividualProgramProcess().getPerson();

	if (StringUtils.isEmpty(person.getGivenNames())) {
	    String[] parts = person.getName().split("\\s+");
	    int split = parts.length > 3 ? 2 : 1;
	    setGivenNames(StringUtils.join(Arrays.copyOfRange(parts, 0, split), " "));
	    setFamilyNames(StringUtils.join(Arrays.copyOfRange(parts, split, parts.length), " "));
	} else {
	    setGivenNames(person.getGivenNames());
	    setFamilyNames(person.getFamilyNames());
	}
    }

    public String getGivenNames() {
	return givenNames;
    }

    public void setGivenNames(String givenNames) {
	this.givenNames = givenNames;
    }

    public String getFamilyNames() {
	return familyNames;
    }

    public void setFamilyNames(String familyNames) {
	this.familyNames = familyNames;
    }

    public DocumentRequestType getDocumentRequestType() {
	return documentRequestType;
    }

    public void setDocumentRequestType(DocumentRequestType documentRequestType) {
	this.documentRequestType = documentRequestType;
    }

    @Service
    @Override
    public PhdDocumentRequest createNewRequest() {
	switch (getDocumentRequestType()) {
	case DIPLOMA_REQUEST:
	    return PhdDiplomaRequest.create(this);
	case REGISTRY_DIPLOMA_REQUEST:
	    return PhdRegistryDiplomaRequest.create(this);
	default:
	    throw new DomainException("error.PhdAcademicServiceRequest.create.document.request.type.unknown");
	}
    }
}
