package DataBeans.transactions;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import Dominio.transactions.IInsuranceTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InfoInsuranceTransaction extends InfoPaymentTransaction {

    private InfoExecutionYear infoExecutionYear;

    private InfoStudent infoStudent;

    public InfoInsuranceTransaction() {

    }

    protected void copyFromDomain(IInsuranceTransaction insuranceTransaction) {

        super.copyFromDomain(insuranceTransaction);

        this.infoExecutionYear = InfoExecutionYear.newInfoFromDomain(insuranceTransaction
                .getExecutionYear());
        this.infoStudent = InfoStudent.newInfoFromDomain(insuranceTransaction.getStudent());
    }

    public static InfoInsuranceTransaction newInfoFromDomain(IInsuranceTransaction insuranceTransaction) {

        if (insuranceTransaction == null) {
            return null;
        }

        InfoInsuranceTransaction infoInsuranceTransaction = new InfoInsuranceTransaction();
        infoInsuranceTransaction.copyFromDomain(insuranceTransaction);

        return infoInsuranceTransaction;
    }

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }
}