package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> <br/>
 *         <strong>Description: </strong> <br/>
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
public class EditReimbursementGuide extends Service {

    public void run(Integer reimbursementGuideId, String situation, Date officialDate, String remarks,
            IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
        ReimbursementGuide reimbursementGuide = rootDomainObject
                .readReimbursementGuideByOID(reimbursementGuideId);
        if (reimbursementGuide == null) {
            throw new NonExistingServiceException();
        }

        ReimbursementGuideSituation activeSituation = reimbursementGuide
                .getActiveReimbursementGuideSituation();

        if (!validateReimbursementGuideSituation(activeSituation, situation)) {
            throw new InvalidGuideSituationServiceException();
        }
        ReimbursementGuideSituation newActiveSituation = new ReimbursementGuideSituation();

        Person person = Person.readPersonByUsername(userView.getUtilizador());
        Employee employee = person.getEmployee();

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

        newActiveSituation.setRemarks(remarks);

        // REIMBURSEMENT TRANSACTIONS
        if (newState.equals(ReimbursementGuideState.PAYED)) {
            List reimbursementGuideEntries = reimbursementGuide.getReimbursementGuideEntries();
            Iterator iterator = reimbursementGuideEntries.iterator();
            ReimbursementGuideEntry reimbursementGuideEntry = null;
            ReimbursementTransaction reimbursementTransaction = null;

            PersonAccount personAccount = reimbursementGuide.getGuide().getPerson()
                    .getAssociatedPersonAccount();

            if (personAccount == null) {
                personAccount = new PersonAccount(reimbursementGuide.getGuide()
                        .getPerson());
            }

            while (iterator.hasNext()) {
                reimbursementGuideEntry = (ReimbursementGuideEntry) iterator.next();

                if (checkReimbursementGuideEntriesSum(reimbursementGuideEntry) == false) {
                    throw new InvalidReimbursementValueServiceException(
                            "error.exception.masterDegree.invalidReimbursementValue");

                }
                if (reimbursementGuideEntry.getGuideEntry().getDocumentType().equals(
                        DocumentType.GRATUITY)) {

                    // // because of an OJB with cache bug we have to read
                    // the guide entry again
                    // reimbursementGuideEntry = (ReimbursementGuideEntry)
                    // reimbursementGuideEntryDAO
                    // .readByOID(ReimbursementGuideEntry.class,
                    // reimbursementGuideEntry.getIdInternal());

                    reimbursementTransaction = new ReimbursementTransaction(
                            reimbursementGuideEntry.getValue(), new Timestamp(Calendar.getInstance()
                                    .getTimeInMillis()), "", reimbursementGuideEntry.getGuideEntry()
                                    .getGuide().getPaymentType(),
                            TransactionType.GRATUITY_REIMBURSEMENT, Boolean.FALSE, person,
                            personAccount, reimbursementGuideEntry);

                    Person studentPerson = reimbursementGuide.getGuide().getPerson();
                    Registration registration = studentPerson.readStudentByDegreeType(DegreeType.MASTER_DEGREE);
                    ExecutionDegree executionDegree = reimbursementGuide.getGuide().getExecutionDegree();

                    GratuitySituation gratuitySituation = registration
                            .readGratuitySituationByExecutionDegree(executionDegree);

                    if (gratuitySituation == null) {
                        throw new FenixServiceException(
                                "Database is inconsistent. The gratuity situation is supposed to exist");
                    }
                    
                    gratuitySituation.updateValues();

                }

                if (reimbursementGuideEntry.getGuideEntry().getDocumentType().equals(
                        DocumentType.INSURANCE)) {

                    reimbursementTransaction = new ReimbursementTransaction(
                            reimbursementGuideEntry.getValue(), new Timestamp(Calendar.getInstance()
                                    .getTimeInMillis()), "", reimbursementGuideEntry.getGuideEntry()
                                    .getGuide().getPaymentType(),
                            TransactionType.INSURANCE_REIMBURSEMENT, Boolean.FALSE, person,
                            personAccount, reimbursementGuideEntry);

                }
            }
        }

        activeSituation.setState(new State(State.INACTIVE_STRING));
        newActiveSituation.setReimbursementGuide(reimbursementGuide);
        newActiveSituation.setState(new State(State.ACTIVE));

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
    private boolean validateReimbursementGuideSituation(ReimbursementGuideSituation activeSituation,
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
     * @throws ExcepcaoPersistencia
     */
    private boolean checkReimbursementGuideEntriesSum(ReimbursementGuideEntry reimbursementGuideEntry)
            throws FenixServiceException, ExcepcaoPersistencia {
        GuideEntry guideEntry = reimbursementGuideEntry.getGuideEntry();
        Double guideEntryValue = new Double(guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue());
        Double sum = reimbursementGuideEntry.getValue();

        List reimbursementGuideEntries = guideEntry.getReimbursementGuideEntries();

        if (reimbursementGuideEntries == null) {
            return isGreaterThan(guideEntryValue, sum);
        }

        Iterator it = reimbursementGuideEntries.iterator();
        while (it.hasNext()) {
            ReimbursementGuideEntry reimbursementGuideEntryTmp = (ReimbursementGuideEntry) it.next();

            // because of an OJB with cache bug we have to read the guide entry
            // again
            reimbursementGuideEntryTmp = rootDomainObject
                    .readReimbursementGuideEntryByOID(reimbursementGuideEntryTmp.getIdInternal());

            if (reimbursementGuideEntryTmp.getReimbursementGuide()
                    .getActiveReimbursementGuideSituation().getReimbursementGuideState().equals(
                            ReimbursementGuideState.PAYED)) {
                sum = new Double(sum.doubleValue() + reimbursementGuideEntryTmp.getValue().doubleValue());
            }

        }

        return isGreaterThan(guideEntryValue, sum);

    }

    private boolean isGreaterThan(Double guideEntryValue, Double sum) {
        if (sum.doubleValue() > guideEntryValue.doubleValue()) {
            return false;
        }
        return true;
    }

}