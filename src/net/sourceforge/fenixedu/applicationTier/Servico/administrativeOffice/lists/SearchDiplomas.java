package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchDiplomasBySituationParametersBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchDiplomas extends FenixService {

    @Service
    public static Collection<DiplomaRequest> run(final SearchDiplomasBySituationParametersBean bean) {
	final Collection<DiplomaRequest> result = new HashSet<DiplomaRequest>();

	for (final AcademicServiceRequest request : bean.searchAcademicServiceRequests()) {
	    if (!request.isRequestForRegistration()) {
		continue;
	    }
	    if (!request.isDocumentRequest() || !((DocumentRequest) request).isDiploma()) {
		continue;
	    }
	    DiplomaRequest diplomaRequest = (DiplomaRequest) request;

	    DegreeType diplomaDegreeType = diplomaRequest.getRegistration().getDegree().getDegreeType();
	    if ((diplomaDegreeType != DegreeType.EMPTY) && (!getAdministratedDegreeTypes().contains(diplomaDegreeType))) {
		continue;
	    }

	    result.add(diplomaRequest);
	}

	return result;
    }

    private static Collection<DegreeType> getAdministratedDegreeTypes() {
	return AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegreeTypes();
    }

}
