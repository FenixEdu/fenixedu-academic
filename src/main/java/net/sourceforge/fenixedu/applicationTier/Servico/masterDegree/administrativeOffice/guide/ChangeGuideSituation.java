/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ChangeGuideSituation {

    @Atomic
    public static void run(Integer guideNumber, Integer guideYear, Integer guideVersion, Date paymentDate, String remarks,
            String situationOfGuideString, String paymentType, IUserView userView) throws ExcepcaoInexistente,
            FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        Guide guide = Guide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);

        if (guide == null) {
            throw new ExcepcaoInexistente("Unknown Guide !!");
        }

        GuideSituation activeGuideSituation = (GuideSituation) CollectionUtils.find(guide.getGuideSituations(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                GuideSituation guideSituation = (GuideSituation) arg0;
                return guideSituation.getState().getState().equals(State.ACTIVE);
            }

        });

        GuideState newSituationOfGuide = GuideState.valueOf(situationOfGuideString);

        // check if the change is valid
        if (!verifyChangeValidation(activeGuideSituation, newSituationOfGuide)) {
            throw new NonValidChangeServiceException();
        }

        if (newSituationOfGuide.equals(activeGuideSituation.getSituation())) {
            activeGuideSituation.setRemarks(remarks);
            if (activeGuideSituation.getSituation().equals(GuideState.PAYED)) {
                guide.setPaymentDate(paymentDate);
                guide.setPaymentType(PaymentType.valueOf(paymentType));
            }
            // guide.addGuideSituations(activeGuideSituation);
        } else {

            // Create The New Situation
            activeGuideSituation.setState(new State(State.INACTIVE));

            GuideSituation newGuideSituation = new GuideSituation();

            Calendar date = Calendar.getInstance();
            newGuideSituation.setDate(date.getTime());
            newGuideSituation.setRemarks(remarks);
            newGuideSituation.setSituation(newSituationOfGuide);
            newGuideSituation.setState(new State(State.ACTIVE));

            if (newSituationOfGuide.equals(GuideState.PAYED)) {
                guide.setPaymentDate(paymentDate);
                guide.setPaymentType(PaymentType.valueOf(paymentType));

                // For Transactions Creation
                PersonAccount personAccount = guide.getPerson().getAssociatedPersonAccount();
                if (personAccount == null) {
                    personAccount = new PersonAccount(guide.getPerson());
                }

                Person employeePerson = Person.readPersonByUsername(userView.getUtilizador());
                Person studentPerson = guide.getPerson();
                ExecutionDegree executionDegree = guide.getExecutionDegree();
                Registration registration =
                        studentPerson.readRegistrationByDegreeCurricularPlan(executionDegree.getDegreeCurricularPlan());

                // Iterate Guide Entries to create Transactions
                for (GuideEntry guideEntry : guide.getGuideEntries()) {

                    // Write Gratuity Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY)) {

                        GratuitySituation gratuitySituation =
                                registration.readGratuitySituationByExecutionDegree(executionDegree);

                        Double value = new Double(guideEntry.getPrice().doubleValue() * guideEntry.getQuantity().intValue());

                        PaymentTransaction paymentTransaction =
                                new GratuityTransaction(value, new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                        guideEntry.getDescription(), guide.getPaymentType(),
                                        TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, employeePerson, personAccount,
                                        guideEntry, gratuitySituation);

                        gratuitySituation.updateValues();

                    }

                    // Write Insurance Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE)) {

                        List insuranceTransactionList =
                                registration.readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionDegree
                                        .getExecutionYear());

                        if (!insuranceTransactionList.isEmpty()) {
                            throw new ExistingServiceException("error.message.transaction.insuranceTransactionAlreadyExists");
                        }

                        new InsuranceTransaction(guideEntry.getPrice(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                guideEntry.getDescription(), guide.getPaymentType(), TransactionType.INSURANCE_PAYMENT,
                                Boolean.FALSE, guide.getPerson(), personAccount, guideEntry, executionDegree.getExecutionYear(),
                                registration);

                    }
                }
            }

            // Write the new Situation
            guide.addGuideSituations(newGuideSituation);

        }

    }

    private static boolean verifyChangeValidation(GuideSituation activeGuideSituation, GuideState situationOfGuide) {
        if (activeGuideSituation.equals(GuideState.ANNULLED)) {
            return false;
        }

        if ((activeGuideSituation.getSituation().equals(GuideState.PAYED)) && (situationOfGuide.equals(GuideState.NON_PAYED))) {
            return false;
        }

        return true;
    }

}