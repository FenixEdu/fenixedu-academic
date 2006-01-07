package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadMasterDegreeThesisDataVersionByID implements IService {

	public Object run(Integer masterDegreeThesisDataVersionID) throws FenixServiceException, ExcepcaoPersistencia {
		InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
		MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		masterDegreeThesisDataVersion = (MasterDegreeThesisDataVersion) sp
				.getIPersistentMasterDegreeThesisDataVersion().readByOID(
						MasterDegreeThesisDataVersion.class, masterDegreeThesisDataVersionID);

		if (masterDegreeThesisDataVersion == null)
			throw new NonExistingServiceException(
					"error.exception.masterDegree.nonExistingMasterDegreeThesisDataVersion");

		infoMasterDegreeThesisDataVersion = InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
				.newInfoFromDomain(masterDegreeThesisDataVersion);

		return infoMasterDegreeThesisDataVersion;
	}
}