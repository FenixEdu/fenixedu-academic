package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuity;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGratuity;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadActiveGratuityByStudentCurricularPlanID implements IService {

    public InfoGratuity run(Integer studentCurricularPlanID) throws FenixServiceException {

        IGratuity gratuity = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            gratuity = sp.getIPersistentGratuity().readByStudentCurricularPlanIDAndState(
                    studentCurricularPlanID, new State(State.ACTIVE));

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (gratuity == null) {
            throw new NonExistingServiceException();
        }

        return Cloner.copyIGratuity2InfoGratuity(gratuity);
    }
}