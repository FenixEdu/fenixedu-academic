package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.IExecutionYear;
import Dominio.IGuideEntry;
import Dominio.IPersonAccount;
import Dominio.IPessoa;
import Dominio.IStudent;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InsuranceTransaction extends PaymentTransaction implements IInsuranceTransaction {

    private Integer keyExecutionYear;

    private IExecutionYear executionYear;

    private Integer keyStudent;

    private IStudent student;

    public InsuranceTransaction() {

    }

    /**
     * @param value
     * @param transactionDate
     * @param remarks
     * @param paymentType
     * @param transactionType
     * @param wasInternalBalance
     * @param responsiblePerson
     * @param personAccount
     * @param guideEntry
     * @param executionYear
     * @param student
     */
    public InsuranceTransaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            IPessoa responsiblePerson, IPersonAccount personAccount, IGuideEntry guideEntry,
            IExecutionYear executionYear, IStudent student) {
        super(value, transactionDate, remarks, paymentType, transactionType, wasInternalBalance,
                responsiblePerson, personAccount, guideEntry);
        this.executionYear = executionYear;
        this.student = student;
    }

    /**
     * @return Returns the executionYear.
     */
    public IExecutionYear getExecutionYear() {
        return executionYear;
    }

    /**
     * @param executionYear
     *            The executionYear to set.
     */
    public void setExecutionYear(IExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the keyExecutionYear.
     */
    public Integer getKeyExecutionYear() {
        return keyExecutionYear;
    }

    /**
     * @param keyExecutionYear
     *            The keyExecutionYear to set.
     */
    public void setKeyExecutionYear(Integer keyExecutionYear) {
        this.keyExecutionYear = keyExecutionYear;
    }

    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /**
     * @param keyStudent
     *            The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }
}