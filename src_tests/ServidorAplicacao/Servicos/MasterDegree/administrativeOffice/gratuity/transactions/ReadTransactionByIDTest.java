package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadTransactionByIDTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadTransactionByIDTest(String testName) {
        super(testName);

        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/transactions/testReadTransactionByIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadTransactionByID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(744) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(744) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer transactionID = new Integer(744);
            Object[] args = { transactionID };
            InfoTransaction infoTransaction = (InfoTransaction) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoTransaction);
            assertEquals(infoTransaction.getIdInternal(), transactionID);

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {

        try {
            Integer transactionID = new Integer(700);
            Object[] args = { transactionID };
            InfoTransaction infoTransaction = (InfoTransaction) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoTransaction);
            assertEquals(infoTransaction.getIdInternal(), transactionID);

        } catch (ExcepcaoInexistente e) {
            // ok
        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }
}