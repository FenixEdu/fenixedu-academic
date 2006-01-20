package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationById extends Service {

	public InfoGratuitySituation run(Integer gratuitySituationID) throws FenixServiceException, ExcepcaoPersistencia {
		InfoGratuitySituation infoGratuitySituation = null;

		IPersistentGratuitySituation persistentGratuitySituation = persistentSupport.getIPersistentGratuitySituation();

		GratuitySituation gratuitySituation = (GratuitySituation) persistentGratuitySituation
				.readByOID(GratuitySituation.class, gratuitySituationID);

		if (gratuitySituation == null) {
			throw new NonExistingServiceException(
					"error.exception.masterDegree.gratuity.notExistingGratuitySituation");
		}

		infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
				.newInfoFromDomain(gratuitySituation);

		return infoGratuitySituation;

	}
}