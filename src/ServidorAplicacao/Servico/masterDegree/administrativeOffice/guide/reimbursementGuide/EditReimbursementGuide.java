/*
 * Created on 20/Nov/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuideSituation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
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
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> <br>
 *         <strong>Description: </strong> <br>
 *         This service edits a reimbursement guide. Editing a reimbursement
 *         guide is in reallity the creation of a new reimbursement guide
 *         situation associated with the reimbursement guide in question. The
 *         former active situation of the reimbursement guide changes state and
 *         a new situation with an active state. Also there are some rules
 *         related with the ReimbursementGuideSituationState that the service
 *         enforces. The allowed states are: a) if the current state is issued
 *         it can be changed to approved,payed and annulled b) if the current
 *         state is approved it can be changed to payed and annuled c) if the
 *         current state is payed it cannot be changed d) if the current state
 *         is annuled it cannot be changed
 */
public class EditReimbursementGuide implements IService {

    public EditReimbursementGuide() {
    }

    /**
     * @throws FenixServiceException,
     *             InvalidGuideSituationServiceException
     */

    public void run(Integer reimbursementGuideId, String situation,
            Date officialDate, String remarks, IUserView userView)
            throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();

            IPersistentReimbursementGuide persistentReimbursementGuide = ps
                    .getIPersistentReimbursementGuide();
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) persistentReimbursementGuide
                    .readByOID(ReimbursementGuide.class, reimbursementGuideId,
                            true);

            if (reimbursementGuide == null) {
                throw new NonExistingServiceException();
            }

            IPersistentReimbursementGuideSituation persistentReimbursementGuideSituation = ps
                    .getIPersistentReimbursementGuideSituation();
            IReimbursementGuideSituation activeSituation = reimbursementGuide
                    .getActiveReimbursementGuideSituation();

            if (!validateReimbursementGuideSituation(activeSituation, situation)) {
                throw new InvalidGuideSituationServiceException();
            } else {
                persistentReimbursementGuideSituation
                        .simpleLockWrite(activeSituation);
                activeSituation.setState(new State(State.INACTIVE_STRING));
                IReimbursementGuideSituation newActiveSituation = new ReimbursementGuideSituation();

                IPersistentEmployee persistentEmployee = ps
                        .getIPersistentEmployee();
                IPessoaPersistente persistentPerson = ps
                        .getIPessoaPersistente();
                IPessoa person = persistentPerson.lerPessoaPorUsername(userView
                        .getUtilizador());
                IEmployee employee = persistentEmployee.readByPerson(person);

                newActiveSituation.setEmployee(employee);
                newActiveSituation.setModificationDate(Calendar.getInstance());

                if (officialDate != null) {
                    Calendar officialDateCalendar = new GregorianCalendar();
                    officialDateCalendar.setTime(officialDate);
                    newActiveSituation.setOfficialDate(officialDateCalendar);
                } else {
                    newActiveSituation.setOfficialDate(Calendar.getInstance());
                }

                ReimbursementGuideState newState = ReimbursementGuideState
                        .getEnum(situation);
                newActiveSituation.setReimbursementGuideState(newState);

                newActiveSituation.setReimbursementGuide(reimbursementGuide);
                newActiveSituation.setState(new State(State.ACTIVE));
                newActiveSituation.setRemarks(remarks);

                List reimbursementGuideSituations = reimbursementGuide
                        .getReimbursementGuideSituations();
                reimbursementGuideSituations.add(newActiveSituation);
                reimbursementGuide
                        .setReimbursementGuideSituations(reimbursementGuideSituations);

                persistentReimbursementGuideSituation
                        .simpleLockWrite(newActiveSituation);

                if (newState.equals(ReimbursementGuideState.PAYED)) {
                    //TODO: create reimbursement transactions
                }

            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param activeSituation
     * @param situation
     * @return The allowed states are: a) if the current state is issued it can
     *         be changed to approved,payed and annulled b) if the current state
     *         is approved it can be changed to payed and annuled c) if the
     *         current state is payed it cannot be changed d) if the current
     *         state is annuled it cannot be changed Also the state doesnt need
     *         to change
     */
    private boolean validateReimbursementGuideSituation(
            IReimbursementGuideSituation activeSituation, String situation) {

        ReimbursementGuideState newState = ReimbursementGuideState
                .getEnum(situation);
        ReimbursementGuideState currentState = activeSituation
                .getReimbursementGuideState();

        if (currentState.equals(newState))
            return false;

        if (currentState.equals(ReimbursementGuideState.ISSUED)) {
            if (newState.equals(ReimbursementGuideState.APPROVED)
                    || newState.equals(ReimbursementGuideState.PAYED)
                    || newState.equals(ReimbursementGuideState.ANNULLED)) {
                return true;
            } else {
                return false;
            }
        }
        if (currentState.equals(ReimbursementGuideState.APPROVED)) {
            if (newState.equals(ReimbursementGuideState.PAYED)
                    || newState.equals(ReimbursementGuideState.ANNULLED)) {
                return true;
            } else {
                return false;
            }
        }
        if (currentState.equals(ReimbursementGuideState.PAYED)) {
            return false;
        }
        if (currentState.equals(ReimbursementGuideState.ANNULLED)) {
            return false;
        }
        return false;
    }

}