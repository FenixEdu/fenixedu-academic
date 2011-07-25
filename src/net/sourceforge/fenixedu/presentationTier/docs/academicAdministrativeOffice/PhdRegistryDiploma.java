package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

public class PhdRegistryDiploma extends RegistryDiploma {

    private static final long serialVersionUID = 1L;

    protected PhdRegistryDiploma(IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected PhdRegistryDiplomaRequest getDocumentRequest() {
	return (PhdRegistryDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	addParameter("thesisTitle", getDocumentRequest().getPhdIndividualProgramProcess().getThesisTitle());
    }

}
