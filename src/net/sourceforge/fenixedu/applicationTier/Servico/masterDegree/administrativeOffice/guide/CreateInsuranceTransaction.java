/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateInsuranceTransaction extends Service {

    public void run(Integer guideEntryID, IUserView userView) throws ExcepcaoPersistencia,
            ExistingServiceException {

        GuideEntry guideEntry = rootDomainObject.readGuideEntryByOID(guideEntryID);
        Guide guide = guideEntry.getGuide();

        Student student = guide.getPerson().readStudentByDegreeType(DegreeType.MASTER_DEGREE);
        Person responsible = Person.readPersonByUsername(userView.getUtilizador());

        List insuranceTransactionList = student
                .readAllNonReimbursedInsuranceTransactionsByExecutionYear(guide.getExecutionDegree()
                        .getExecutionYear());

        if (insuranceTransactionList.isEmpty() == false) {
            throw new ExistingServiceException(
                    "error.message.transaction.insuranceTransactionAlreadyExists");
        }

        PersonAccount personAccount = guide.getPerson().getAssociatedPersonAccount();

        if (personAccount == null) {
            personAccount = new PersonAccount(guide.getPerson());
        }

        new InsuranceTransaction(guideEntry.getPrice(), new Timestamp(Calendar
                .getInstance().getTimeInMillis()), guideEntry.getDescription(), guide.getPaymentType(),
                TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, responsible, personAccount,
                guideEntry, guide.getExecutionDegree().getExecutionYear(), student);
    }

}
