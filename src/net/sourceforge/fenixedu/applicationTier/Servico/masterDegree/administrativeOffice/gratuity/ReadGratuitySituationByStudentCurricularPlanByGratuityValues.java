/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues implements IService {

    public Object run(Integer studentCurricularPlanID, Integer gratuityValuesID)
            throws FenixServiceException {
        ISuportePersistente sp = null;
        IGratuitySituation gratuitySituation = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            gratuitySituation = persistentGratuitySituation
                    .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                            studentCurricularPlanID, gratuityValuesID);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                    .newInfoFromDomain(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}
