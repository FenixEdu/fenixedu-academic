/*
 * Created on Jan 20, 2005
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IPersonAccount;
import Dominio.IPerson;
import Dominio.IStudent;
import Dominio.transactions.IInsuranceTransaction;
import Dominio.transactions.InsuranceTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.transactions.IPersistentInsuranceTransaction;
import Util.TipoCurso;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateInsuranceTransaction implements IService {

    public void run(Integer guideEntryID, IUserView userView) throws ExcepcaoPersistencia,
            ExistingServiceException {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);
        IGuide guide = guideEntry.getGuide();

        IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                .getIPersistentInsuranceTransaction();

        IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(guide.getPerson(),
                TipoCurso.MESTRADO_OBJ);
        IPerson responsible = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

        List insuranceTransactionList = insuranceTransactionDAO
                .readAllNonReimbursedByExecutionYearAndStudent(guide.getExecutionDegree()
                        .getExecutionYear(), student);

        if (insuranceTransactionList.isEmpty() == false) {
            throw new ExistingServiceException(
                    "error.message.transaction.insuranceTransactionAlreadyExists");
        }

        IPersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(guide.getPerson());

        IInsuranceTransaction insuranceTransaction = new InsuranceTransaction(guideEntry.getPrice(),
                new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry.getDescription(),
                guide.getPaymentType(), TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, responsible,
                personAccount, guideEntry, guide.getExecutionDegree().getExecutionYear(), student);

        sp.getIPersistentInsuranceTransaction().lockWrite(insuranceTransaction);

    }

}
