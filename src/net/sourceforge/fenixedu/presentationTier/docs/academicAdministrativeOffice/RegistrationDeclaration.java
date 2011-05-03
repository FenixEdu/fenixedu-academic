package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationDeclaration extends AdministrativeOfficeDocument {

    protected RegistrationDeclaration(final IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
	return (DocumentRequest) super.getDocumentRequest();
    }

    protected String getDegreeDescription() {
	final Registration registration = getDocumentRequest().getRegistration();
	final DegreeType degreeType = registration.getDegreeType();
	final CycleType cycleType = degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
		.getCycleType(getExecutionYear());
	return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
    }

}
