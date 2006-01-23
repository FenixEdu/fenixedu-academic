package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadInsuranceTransactionByStudentIDAndExecutionYearID extends Service {

	/**
	 * Constructor
	 */
	public ReadInsuranceTransactionByStudentIDAndExecutionYearID() {
	}

	public InfoInsuranceTransaction run(Integer studentId, Integer executionYearId)
			throws FenixServiceException, ExcepcaoPersistencia {

		InfoInsuranceTransaction infoInsuranceTransaction = null;

		IPersistentInsuranceTransaction insuranceTransactionDAO = persistentSupport
				.getIPersistentInsuranceTransaction();

		ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
				ExecutionYear.class, executionYearId);

		Student student = (Student) persistentObject.readByOID(Student.class, studentId);

		if ((executionYear == null) || (student == null)) {
			return null;
		}

		List insuranceTransactionList = insuranceTransactionDAO
				.readAllNonReimbursedByExecutionYearAndStudent(executionYear.getIdInternal(), student
						.getIdInternal());

		if (insuranceTransactionList.size() > 1) {
			throw new FenixServiceException(
					"Database is incoerent. Its not supposed to exist more than one insurance transaction not reimbursed");

		}

		if (insuranceTransactionList.size() == 1) {
			infoInsuranceTransaction = InfoInsuranceTransaction
					.newInfoFromDomain((InsuranceTransaction) insuranceTransactionList.get(0));
		}

		return infoInsuranceTransaction;
	}
}