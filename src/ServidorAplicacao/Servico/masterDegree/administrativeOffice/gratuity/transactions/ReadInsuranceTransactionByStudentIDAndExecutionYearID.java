package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.transactions.InfoInsuranceTransaction;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.Student;
import Dominio.transactions.IInsuranceTransaction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.transactions.IPersistentInsuranceTransaction;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadInsuranceTransactionByStudentIDAndExecutionYearID implements IService {

    /**
     * Constructor
     */
    public ReadInsuranceTransactionByStudentIDAndExecutionYearID() {
    }

    public InfoInsuranceTransaction run(Integer studentId, Integer executionYearId)
            throws FenixServiceException {

        InfoInsuranceTransaction infoInsuranceTransaction = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                    .getIPersistentInsuranceTransaction();

            IExecutionYear executionYear = (IExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                    ExecutionYear.class, executionYearId);

            IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class, studentId);

            if ((executionYear == null) || (student == null)) {
                return null;
            }

            List insuranceTransactionList = insuranceTransactionDAO
                    .readAllNonReimbursedByExecutionYearAndStudent(executionYear, student);

            if (insuranceTransactionList.size() > 1) {
                throw new FenixServiceException(
                        "Database is incoerent. Its not supposed to exist more than one insurance transaction not reimbursed");

            }

            if (insuranceTransactionList.size() == 1) {
                infoInsuranceTransaction = InfoInsuranceTransaction
                        .newInfoFromDomain((IInsuranceTransaction) insuranceTransactionList.get(0));
            }

            return infoInsuranceTransaction;

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}