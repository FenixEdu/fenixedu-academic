package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.transactions.IInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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