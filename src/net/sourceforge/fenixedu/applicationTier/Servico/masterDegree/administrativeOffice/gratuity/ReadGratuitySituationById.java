package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationById implements IService {

	public InfoGratuitySituation run(Integer gratuitySituationID) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = null;
		InfoGratuitySituation infoGratuitySituation = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		GratuitySituation gratuitySituation = (GratuitySituation) persistentGratuitySituation
				.readByOID(GratuitySituation.class, gratuitySituationID, true);

		if (gratuitySituation == null) {
			throw new NonExistingServiceException(
					"error.exception.masterDegree.gratuity.notExistingGratuitySituation");
		}

		infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
				.newInfoFromDomain(gratuitySituation);

		return infoGratuitySituation;

	}
}