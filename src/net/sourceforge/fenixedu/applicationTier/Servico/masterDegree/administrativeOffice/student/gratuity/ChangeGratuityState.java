package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.gratuity;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.Gratuity;
import net.sourceforge.fenixedu.domain.IGratuity;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.GratuityState;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChangeGratuityState implements IService {

    /**
     * The actor of this class.
     */
    public ChangeGratuityState() {
    }

    public void run(Integer studentCurricularPlanID, GratuityState gratuityState, String othersRemarks)
            throws FenixServiceException {

        IGratuity gratuity = null;
        ISuportePersistente sp = null;
        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            gratuity = sp.getIPersistentGratuity().readByStudentCurricularPlanIDAndState(
                    studentCurricularPlanID, new State(State.ACTIVE));

            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        if (gratuity != null) {

            if (gratuity.getGratuityState().equals(gratuityState)) {
                throw new InvalidChangeServiceException();
            }

            try {
                sp.getIPersistentGratuity().simpleLockWrite(gratuity);
                gratuity.setState(new State(State.INACTIVE));

            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException(e);
            }
        }

        IGratuity newGratuity = new Gratuity();
        try {
            sp.getIPersistentGratuity().simpleLockWrite(newGratuity);
            newGratuity.setDate(Calendar.getInstance().getTime());
            newGratuity.setState(new State(State.ACTIVE));
            newGratuity.setRemarks(othersRemarks);
            newGratuity.setStudentCurricularPlan(studentCurricularPlan);
            newGratuity.setGratuityState(gratuityState);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}