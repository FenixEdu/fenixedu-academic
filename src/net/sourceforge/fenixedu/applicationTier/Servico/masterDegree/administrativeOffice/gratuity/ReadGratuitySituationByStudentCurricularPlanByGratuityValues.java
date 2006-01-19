/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
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
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues extends Service {

	public Object run(Integer studentCurricularPlanID, Integer gratuityValuesID)
			throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = null;
		GratuitySituation gratuitySituation = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		gratuitySituation = persistentGratuitySituation
				.readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(studentCurricularPlanID,
						gratuityValuesID);

		InfoGratuitySituation infoGratuitySituation = null;
		if (gratuitySituation != null) {
			infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
					.newInfoFromDomain(gratuitySituation);
		}

		return infoGratuitySituation;
	}
}
