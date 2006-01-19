package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.applicationTier.IService;

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
			throws FenixServiceException, ExcepcaoPersistencia {

		InfoInsuranceTransaction infoInsuranceTransaction = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentInsuranceTransaction insuranceTransactionDAO = sp
				.getIPersistentInsuranceTransaction();

		ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentExecutionYear().readByOID(
				ExecutionYear.class, executionYearId);

		Student student = (Student) sp.getIPersistentStudent().readByOID(Student.class, studentId);

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