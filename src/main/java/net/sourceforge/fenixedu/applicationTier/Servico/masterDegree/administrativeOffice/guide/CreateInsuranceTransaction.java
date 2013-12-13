/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateInsuranceTransaction {

    @Atomic
    public static void run(String guideEntryID, User userView) throws ExistingServiceException {
        check(RolePredicates.MANAGER_PREDICATE);

        GuideEntry guideEntry = FenixFramework.getDomainObject(guideEntryID);
        Guide guide = guideEntry.getGuide();

        Registration registration = guide.getPerson().readStudentByDegreeType(DegreeType.MASTER_DEGREE);
        Person responsible = Person.readPersonByUsername(userView.getUsername());

        List insuranceTransactionList =
                registration.readAllNonReimbursedInsuranceTransactionsByExecutionYear(guide.getExecutionDegree()
                        .getExecutionYear());

        if (insuranceTransactionList.isEmpty() == false) {
            throw new ExistingServiceException("error.message.transaction.insuranceTransactionAlreadyExists");
        }

        PersonAccount personAccount = guide.getPerson().getAssociatedPersonAccount();

        if (personAccount == null) {
            personAccount = new PersonAccount(guide.getPerson());
        }

        new InsuranceTransaction(guideEntry.getPrice(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
                guideEntry.getDescription(), guide.getPaymentType(), TransactionType.INSURANCE_PAYMENT, Boolean.FALSE,
                responsible, personAccount, guideEntry, guide.getExecutionDegree().getExecutionYear(), registration);
    }

}