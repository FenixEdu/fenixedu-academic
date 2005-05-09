/*
 * Created on 20/Nov/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.transactions.IReimbursementTransaction;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonAccount;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

    public void run(Integer reimbursementGuideId, String situation, Date officialDate, String remarks,
            IUserView userView) throws FenixServiceException {
        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentReimbursementGuide persistentReimbursementGuide = ps
                    .getIPersistentReimbursementGuide();

            IPersistentReimbursementGuideEntry reimbursementGuideEntryDAO = ps
                    .getIPersistentReimbursementGuideEntry();
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) persistentReimbursementGuide
                    .readByOID(ReimbursementGuide.class, reimbursementGuideId);

            reimbursementGuideEntryDAO.simpleLockWrite(reimbursementGuide);

            if (reimbursementGuide == null) {
                throw new NonExistingServiceException();
            }
            IPersistentObject persistentObject = ps.getIPersistentObject();

            IReimbursementGuideSituation activeSituation = reimbursementGuide
                    .getActiveReimbursementGuideSituation();

            if (!validateReimbursementGuideSituation(activeSituation, situation)) {
                throw new InvalidGuideSituationServiceException();
            }
            IReimbursementGuideSituation newActiveSituation = new ReimbursementGuideSituation();

            IPersistentEmployee persistentEmployee = ps.getIPersistentEmployee();
            IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
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

            ReimbursementGuideState newState = ReimbursementGuideState.valueOf(situation);
            newActiveSituation.setReimbursementGuideState(newState);

            newActiveSituation.setReimbursementGuide(reimbursementGuide);
            newActiveSituation.setState(new State(State.ACTIVE));
            newActiveSituation.setRemarks(remarks);

            //persistentObject.simpleLockWrite(newActiveSituation);

            // REIMBURSEMENT TRANSACTIONS
            if (newState.equals(ReimbursementGuideState.PAYED)) {
                List reimbursementGuideEntries = reimbursementGuide.getReimbursementGuideEntries();
                Iterator iterator = reimbursementGuideEntries.iterator();
                IReimbursementGuideEntry reimbursementGuideEntry = null;
                IReimbursementTransaction reimbursementTransaction = null;

                IPersistentPersonAccount persistentPersonAccount = ps.getIPersistentPersonAccount();
                IPersonAccount personAccount = persistentPersonAccount.readByPerson(reimbursementGuide
                        .getGuide().getPerson());

                IPersistentReimbursementTransaction persistentReimbursementTransaction = ps
                        .getIPersistentReimbursementTransaction();

                IPersistentGratuitySituation persistentGratuitySituation = ps
                        .getIPersistentGratuitySituation();

                while (iterator.hasNext()) {
                    reimbursementGuideEntry = (IReimbursementGuideEntry) iterator.next();

                    if (checkReimbursementGuideEntriesSum(reimbursementGuideEntry, ps) == false) {
                        throw new InvalidReimbursementValueServiceException(
                                "error.exception.masterDegree.invalidReimbursementValue");

                    }
                    if (reimbursementGuideEntry.getGuideEntry().getDocumentType().equals(
                            DocumentType.GRATUITY)) {

                        //                        // because of an OJB with cache bug we have to read
                        // the guide entry again
                        //                        reimbursementGuideEntry = (IReimbursementGuideEntry)
                        // reimbursementGuideEntryDAO
                        //                                .readByOID(ReimbursementGuideEntry.class,
                        //                                        reimbursementGuideEntry.getIdInternal());

                        reimbursementTransaction = new ReimbursementTransaction(reimbursementGuideEntry
                                .getValue(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                "", reimbursementGuideEntry.getGuideEntry().getGuide().getPaymentType(),
                                TransactionType.GRATUITY_REIMBURSEMENT, Boolean.FALSE, person,
                                personAccount, reimbursementGuideEntry);

                        persistentReimbursementTransaction.lockWrite(reimbursementTransaction);

                        IPerson studentPerson = reimbursementGuide.getGuide().getPerson();
                        IStudent student = ps.getIPersistentStudent().readByPersonAndDegreeType(
                                studentPerson, DegreeType.MASTER_DEGREE);
                        IExecutionDegree executionDegree = reimbursementGuide.getGuide()
                                .getExecutionDegree();

                        IGratuitySituation gratuitySituation = persistentGratuitySituation
                                .readGratuitySituationByExecutionDegreeAndStudent(executionDegree,
                                        student);

                        if (gratuitySituation == null) {
                            throw new FenixServiceException(
                                    "Database is inconsistent. The gratuity situation is supposed to exist");
                        }

                        persistentGratuitySituation.lockWrite(gratuitySituation);

                        Double remainingValue = gratuitySituation.getRemainingValue();

                        gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                                + reimbursementTransaction.getValue().doubleValue()));

                    }

                    if (reimbursementGuideEntry.getGuideEntry().getDocumentType().equals(
                            DocumentType.INSURANCE)) {

                        reimbursementTransaction = new ReimbursementTransaction(reimbursementGuideEntry
                                .getValue(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                "", reimbursementGuideEntry.getGuideEntry().getGuide().getPaymentType(),
                                TransactionType.INSURANCE_REIMBURSEMENT, Boolean.FALSE, person,
                                personAccount, reimbursementGuideEntry);

                        persistentReimbursementTransaction.lockWrite(reimbursementTransaction);
                    }
                }
            }

            persistentObject.simpleLockWrite(activeSituation);
            activeSituation.setState(new State(State.INACTIVE_STRING));

            List reimbursementGuideSituations = reimbursementGuide.getReimbursementGuideSituations();
            reimbursementGuideSituations.add(newActiveSituation);
            reimbursementGuide.setReimbursementGuideSituations(reimbursementGuideSituations);

            persistentObject.simpleLockWrite(newActiveSituation);

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
    private boolean validateReimbursementGuideSituation(IReimbursementGuideSituation activeSituation,
            String situation) {

        ReimbursementGuideState newState = ReimbursementGuideState.valueOf(situation);
        ReimbursementGuideState currentState = activeSituation.getReimbursementGuideState();

        if (currentState.equals(newState))
            return false;

        if (currentState.equals(ReimbursementGuideState.ISSUED)) {
            if (newState.equals(ReimbursementGuideState.APPROVED)
                    || newState.equals(ReimbursementGuideState.PAYED)
                    || newState.equals(ReimbursementGuideState.ANNULLED)) {
                return true;
            }
            return false;

        }
        if (currentState.equals(ReimbursementGuideState.APPROVED)) {
            if (newState.equals(ReimbursementGuideState.PAYED)
                    || newState.equals(ReimbursementGuideState.ANNULLED)) {
                return true;
            }
            return false;

        }
        if (currentState.equals(ReimbursementGuideState.PAYED)) {
            return false;
        }
        if (currentState.equals(ReimbursementGuideState.ANNULLED)) {
            return false;
        }
        return false;
    }

    /**
     * @param reimbursementGuideEntry
     * @param suportePersistente
     * @return true if the sum of existents reeimbursement guide entries of a
     *         guide entry with the new reimbursement guide entry is less or
     *         equal than their guide entry
     */
    private boolean checkReimbursementGuideEntriesSum(IReimbursementGuideEntry reimbursementGuideEntry,
            ISuportePersistente suportePersistente) throws FenixServiceException {
        IPersistentGuideEntry persistentGuideEntry = suportePersistente.getIPersistentGuideEntry();
        IPersistentReimbursementGuideEntry persistentReimbursementGuideEntry = suportePersistente
                .getIPersistentReimbursementGuideEntry();

        IGuideEntry guideEntry = null;
        List reimbursementGuideEntries = null;

        try {
            guideEntry = (IGuideEntry) persistentGuideEntry.readByOID(GuideEntry.class,
                    reimbursementGuideEntry.getGuideEntry().getIdInternal());

            reimbursementGuideEntries = persistentReimbursementGuideEntry.readByGuideEntry(guideEntry);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        Iterator it = reimbursementGuideEntries.iterator();
        Double sum = reimbursementGuideEntry.getValue();

        while (it.hasNext()) {
            IReimbursementGuideEntry reimbursementGuideEntryTmp = (IReimbursementGuideEntry) it.next();

            try {
                // because of an OJB with cache bug we have to read the guide
                // entry again
                reimbursementGuideEntryTmp = (IReimbursementGuideEntry) persistentReimbursementGuideEntry
                        .readByOID(ReimbursementGuideEntry.class, reimbursementGuideEntryTmp
                                .getIdInternal());
            } catch (ExcepcaoPersistencia e1) {
                throw new FenixServiceException(e1);
            }

            if (reimbursementGuideEntryTmp.getReimbursementGuide()
                    .getActiveReimbursementGuideSituation().getReimbursementGuideState().equals(
                            ReimbursementGuideState.PAYED)) {
                sum = new Double(sum.doubleValue() + reimbursementGuideEntryTmp.getValue().doubleValue());
            }

        }

        Double guideEntryValue = new Double(guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue());

        if (sum.doubleValue() > guideEntryValue.doubleValue()) {
            return false;
        }
        return true;

    }

}