/*
 * Created on 20/Nov/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;

import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuideSituation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;
import ServidorPersistente.guide.IPersistentReimbursementGuideSituation;
import Util.ReimbursementGuideState;
import Util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 * 
 * <br>
 * <strong>Description:</strong><br>
 * This service edits a reimbursement guide. Editing a reimbursement guide is in reallity the creation
 * of a new reimbursement guide situation associated with the reimbursement guide in question.
 * The former active situation of the reimbursement guide changes state and a new situation with an 
 * active state. Also there are some rules related with the ReimbursementGuideSituationState that the
 * service enforces. The allowed states are:
 * a) if the current state is issued it can be changed to approved,payed and annulled
 * b) if the current state is approved it can be changed to payed and annuled
 * c) if the current state is payed it cannot be changed
 * d) if the current state is annuled it cannot be changed   
 *
 */
public class EditReimbursementGuide implements IServico
{

    private static EditReimbursementGuide servico = new EditReimbursementGuide();

    /**
     * The singleton access method of this class.
     **/
    public static EditReimbursementGuide getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private EditReimbursementGuide()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "EditReimbursementGuide";
    }
    /**
     *  @throws FenixServiceException, InvalidGuideSituationServiceException
     */

    public void run(Integer reimbursementGuideId, String situation, String remarks, IUserView userView)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentReimbursementGuide persistentReimbursementGuide =
                ps.getIPersistentReimbursementGuide();
            IReimbursementGuide reimbursementGuide = new ReimbursementGuide(reimbursementGuideId);
            reimbursementGuide =
                (IReimbursementGuide) persistentReimbursementGuide.readByOId(reimbursementGuide, false);

            IPersistentReimbursementGuideSituation persistentReimbursementGuideSituation =
                ps.getIPersistentReimbursementGuideSituation();
            IReimbursementGuideSituation activeSituation =
                reimbursementGuide.getActiveReimbursementGuideSituation();

            if (!validateReimbursementGuideSituation(activeSituation, situation))
            {
                throw new InvalidGuideSituationServiceException();
            } else
            {
                persistentReimbursementGuideSituation.simpleLockWrite(activeSituation);
                activeSituation.setState(new State(State.INACTIVE_STRING));
                IReimbursementGuideSituation newActiveSituation = new ReimbursementGuideSituation();
                persistentReimbursementGuideSituation.simpleLockWrite(newActiveSituation);

                IPersistentEmployee persistentEmployee = ps.getIPersistentEmployee();
                IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
                IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
                IEmployee employee = persistentEmployee.readByPerson(person);

                newActiveSituation.setEmployee(employee);
                newActiveSituation.setModificationDate(Calendar.getInstance());
                newActiveSituation.setReimbursementGuide(reimbursementGuide);
                newActiveSituation.setReimbursementGuideState(
                    (ReimbursementGuideState) ReimbursementGuideState.getEnumMap().get(situation));
                newActiveSituation.setState(new State(State.ACTIVE));
                newActiveSituation.setRemarks(remarks);
            }

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param activeSituation
     * @param situation
     * @return
     * 
     * The allowed states are:
     * a) if the current state is issued it can be changed to approved,payed and annulled
     * b) if the current state is approved it can be changed to payed and annuled
     * c) if the current state is payed it cannot be changed
     * d) if the current state is annuled it cannot be changed   
     * 
     * Also the state doesnt need to change
     */
    private boolean validateReimbursementGuideSituation(
        IReimbursementGuideSituation activeSituation,
        String situation)
    {

        ReimbursementGuideState newState = ReimbursementGuideState.getEnum(situation);
        ReimbursementGuideState currentState = activeSituation.getReimbursementGuideState();

        if (currentState.equals(ReimbursementGuideState.ISSUED))
        {
            if (newState.equals(ReimbursementGuideState.ISSUED)
                || newState.equals(ReimbursementGuideState.APPROVED)
                || newState.equals(ReimbursementGuideState.PAYED)
                || newState.equals(ReimbursementGuideState.ANNULLED))
            {
                return true;
            } else
            {
                return false;
            }
        }
        if (currentState.equals(ReimbursementGuideState.APPROVED))
        {
            if (newState.equals(ReimbursementGuideState.APPROVED)
                || newState.equals(ReimbursementGuideState.PAYED)
                || newState.equals(ReimbursementGuideState.ANNULLED))
            {
                return true;
            } else
            {
                return false;
            }
        }
        if (currentState.equals(ReimbursementGuideState.PAYED))
        {
            if (newState.equals(ReimbursementGuideState.PAYED))
            {
                return true;
            } else
            {
                return false;
            }
        }
        if (currentState.equals(ReimbursementGuideState.ANNULLED))
        {
            if (newState.equals(ReimbursementGuideState.ANNULLED))
            {
                return true;
            } else
            {
                return false;
            }
        }
        return false;
    }

}
