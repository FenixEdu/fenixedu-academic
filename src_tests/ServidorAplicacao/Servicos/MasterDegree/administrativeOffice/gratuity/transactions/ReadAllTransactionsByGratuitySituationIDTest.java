package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity.transactions;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllTransactionsByGratuitySituationIDTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadAllTransactionsByGratuitySituationIDTest(String testName) {
        super(testName);

        if (testName.equals("testReadNonExisting")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/transactions/testReadAllTransactionsByGratuitySituationIDNonExistingTransactionsDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/transactions/testReadAllTransactionsByGratuitySituationIDDataSet.xml";
        }

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAllTransactionsByGratuitySituationID";
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
            Integer gratuitySituationID = new Integer(8243);
            Object[] args = { gratuitySituationID };
            List infoTransactionList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoTransactionList);
            assertTrue(infoTransactionList.size() == 2);

            assertEquals(((InfoTransaction) infoTransactionList.get(0))
                    .getIdInternal(), new Integer(841));

            assertEquals(((InfoTransaction) infoTransactionList.get(1))
                    .getIdInternal(), new Integer(782));

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer gratuitySituationID = new Integer(8243);

            Object[] args = { gratuitySituationID };
            List infoInsuranceTransactionList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoInsuranceTransactionList);
            assertTrue(infoInsuranceTransactionList.isEmpty());

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }
}