package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan extends FenixService {

    public InfoMasterDegreeThesisDataVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan)
	    throws FenixServiceException {
	InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(infoStudentCurricularPlan
		.getIdInternal());

	MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = studentCurricularPlan
		.readActiveMasterDegreeThesisDataVersion();

	if (masterDegreeThesisDataVersion == null)
	    throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesis");

	infoMasterDegreeThesisDataVersion = InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
		.newInfoFromDomain(masterDegreeThesisDataVersion);

	return infoMasterDegreeThesisDataVersion;
    }
}