package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadInsuranceTransactionByStudentIDAndExecutionYearIDTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadInsuranceTransactionByStudentIDAndExecutionYearIDTest(
            String testName) {
        super(testName);

        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/transactions/testReadInsuranceTransactionByStudentIDAndExecutionYearIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadInsuranceTransactionByStudentIDAndExecutionYearID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(8949), new Integer(2) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(8949), new Integer(2) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer studentID = new Integer(8949);
            Integer executionYearID = new Integer(2);
            Object[] args = { studentID, executionYearID };
            InfoInsuranceTransaction infoInsuranceTransaction = (InfoInsuranceTransaction) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoInsuranceTransaction);
            assertEquals(infoInsuranceTransaction.getInfoStudent()
                    .getIdInternal(), studentID);
            assertEquals(infoInsuranceTransaction.getInfoExecutionYear()
                    .getIdInternal(), executionYearID);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer studentID = new Integer(80);
            Integer executionYearID = new Integer(2);
            Object[] args = { studentID, executionYearID };
            InfoInsuranceTransaction infoInsuranceTransaction = (InfoInsuranceTransaction) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNull(infoInsuranceTransaction);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }
}