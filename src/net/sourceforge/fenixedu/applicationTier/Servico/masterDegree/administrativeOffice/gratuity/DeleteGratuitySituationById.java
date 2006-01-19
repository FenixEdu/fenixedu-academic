/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Tânia Pousão
 * 
 */
/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGratuitySituationById extends Service {

	public Boolean run(Integer gratuitySituationID) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		GratuitySituation gratuitySituation = (GratuitySituation) persistentGratuitySituation
				.readByOID(GratuitySituation.class, gratuitySituationID);
		if (gratuitySituation == null) {
			return Boolean.TRUE;
		}

		gratuitySituation.setExemptionPercentage(null);
		gratuitySituation.setExemptionValue(null);
		gratuitySituation.setExemptionType(null);
		gratuitySituation.setExemptionDescription(null);

		return Boolean.TRUE;
	}
}
