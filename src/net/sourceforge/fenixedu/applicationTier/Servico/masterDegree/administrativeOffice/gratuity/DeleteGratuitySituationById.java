/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGratuitySituationById extends Service {

	public Boolean run(Integer gratuitySituationID) throws FenixServiceException{
		GratuitySituation gratuitySituation = rootDomainObject.readGratuitySituationByOID(gratuitySituationID);
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
