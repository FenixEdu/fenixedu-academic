/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
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

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            studentCurricularPlan.setIdInternal(studentCurricularPlanID);

            IGratuityValues gratuityValues = new GratuityValues();
            gratuityValues.setIdInternal(gratuityValuesID);

            gratuitySituation = persistentGratuitySituation
                    .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                            studentCurricularPlan, gratuityValues);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation = Cloner
                    .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}