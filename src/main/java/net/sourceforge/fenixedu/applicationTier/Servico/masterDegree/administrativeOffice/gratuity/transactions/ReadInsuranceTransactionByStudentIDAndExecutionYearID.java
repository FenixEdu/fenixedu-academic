package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadInsuranceTransactionByStudentIDAndExecutionYearID {

    /**
     * Constructor
     */
    public ReadInsuranceTransactionByStudentIDAndExecutionYearID() {
    }

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoInsuranceTransaction run(String studentId, String executionYearId) throws FenixServiceException {

        InfoInsuranceTransaction infoInsuranceTransaction = null;

        ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearId);

        Registration registration = AbstractDomainObject.fromExternalId(studentId);

        if ((executionYear == null) || (registration == null)) {
            return null;
        }

        List insuranceTransactionList = registration.readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear);

        if (insuranceTransactionList.size() > 1) {
            throw new FenixServiceException(
                    "Database is incoerent. Its not supposed to exist more than one insurance transaction not reimbursed");

        }

        if (insuranceTransactionList.size() == 1) {
            infoInsuranceTransaction =
                    InfoInsuranceTransaction.newInfoFromDomain((InsuranceTransaction) insuranceTransactionList.get(0));
        }

        return infoInsuranceTransaction;
    }
}