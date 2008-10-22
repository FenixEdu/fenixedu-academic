package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadMasterDegreeProofVersionByID extends FenixService {

    public Object run(Integer masterDegreeProofVersionID) throws FenixServiceException {
	MasterDegreeProofVersion masterDegreeProofVersion = rootDomainObject
		.readMasterDegreeProofVersionByOID(masterDegreeProofVersionID);
	if (masterDegreeProofVersion == null)
	    throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeProofVersion");

	return InfoMasterDegreeProofVersion.newInfoFromDomain(masterDegreeProofVersion);
    }

}
