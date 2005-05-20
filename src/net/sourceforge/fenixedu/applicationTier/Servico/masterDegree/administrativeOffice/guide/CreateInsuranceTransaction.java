/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.transactions.IInsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateInsuranceTransaction implements IService {

    public void run(Integer guideEntryID, IUserView userView) throws ExcepcaoPersistencia,
            ExistingServiceException {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);
        IGuide guide = guideEntry.getGuide();

        IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                .getIPersistentInsuranceTransaction();

        IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(guide.getPerson(),
                DegreeType.MASTER_DEGREE);
        IPerson responsible = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

        List insuranceTransactionList = insuranceTransactionDAO
                .readAllNonReimbursedByExecutionYearAndStudent(guide.getExecutionDegree()
                        .getExecutionYear(), student);

        if (insuranceTransactionList.isEmpty() == false) {
            throw new ExistingServiceException(
                    "error.message.transaction.insuranceTransactionAlreadyExists");
        }

        IPersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(guide.getPerson().getIdInternal());

        IInsuranceTransaction insuranceTransaction = new InsuranceTransaction(guideEntry.getPrice(),
                new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry.getDescription(),
                guide.getPaymentType(), TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, responsible,
                personAccount, guideEntry, guide.getExecutionDegree().getExecutionYear(), student);

        sp.getIPersistentInsuranceTransaction().lockWrite(insuranceTransaction);

    }

}
