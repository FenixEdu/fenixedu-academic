package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.gratuity;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Gratuity;
import Dominio.IGratuity;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GratuityState;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChangeGratuityState implements IService
{

   

    /**
     * The actor of this class.
     */
    public ChangeGratuityState()
    {
    }

   
    public void run(Integer studentCurricularPlanID, GratuityState gratuityState, String othersRemarks)
            throws FenixServiceException
    {

        IGratuity gratuity = null;
        ISuportePersistente sp = null;
        IStudentCurricularPlan studentCurricularPlan = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();

            gratuity = sp.getIPersistentGratuity().readByStudentCurricularPlanIDAndState(
                    studentCurricularPlanID, new State(State.ACTIVE));

            IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
            studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);
            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                    .readByOId(studentCurricularPlanTemp, false);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (gratuity != null)
        {

            if (gratuity.getGratuityState().equals(gratuityState)) { throw new InvalidChangeServiceException(); }

            try
            {
                sp.getIPersistentGratuity().simpleLockWrite(gratuity);
                gratuity.setState(new State(State.INACTIVE));

            }
            catch (ExcepcaoPersistencia e)
            {
                throw new FenixServiceException(e);
            }
        }

        IGratuity newGratuity = new Gratuity();
        try
        {
            sp.getIPersistentGratuity().simpleLockWrite(newGratuity);
            newGratuity.setDate(Calendar.getInstance().getTime());
            newGratuity.setState(new State(State.ACTIVE));
            newGratuity.setRemarks(othersRemarks);
            newGratuity.setStudentCurricularPlan(studentCurricularPlan);
            newGratuity.setGratuityState(gratuityState);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}