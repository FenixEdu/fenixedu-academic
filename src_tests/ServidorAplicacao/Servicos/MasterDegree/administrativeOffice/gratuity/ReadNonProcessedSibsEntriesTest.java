package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.List;

import DataBeans.sibs.InfoSibsPaymentFileEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadNonProcessedSibsEntriesTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadNonProcessedSibsEntriesTest(String testName) {
        super(testName);
        if (testName.equals("testReadNonExisting")) {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadNonProcessedSibsEntriesNonExistingDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadNonProcessedSibsEntriesDataSet.xml";
        }

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadNonProcessedSibsEntries";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = {};

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = {};

        return args;
    }

    public void testReadExisting() {

        try {
            Object[] args = {};
            List infoSibsPaymentFileEntryList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoSibsPaymentFileEntryList);
            assertTrue(infoSibsPaymentFileEntryList.size() == 2);
            assertEquals(
                    ((InfoSibsPaymentFileEntry) infoSibsPaymentFileEntryList
                            .get(0)).getIdInternal(), new Integer(1086));
            assertEquals(
                    ((InfoSibsPaymentFileEntry) infoSibsPaymentFileEntryList
                            .get(1)).getIdInternal(), new Integer(1095));

        } catch (FenixServiceException e) {
            fail("testReadExisting " + e.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Object[] args = {};
            List infoSibsPaymentFileEntryList = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            args);

            assertNotNull(infoSibsPaymentFileEntryList);
            assertTrue(infoSibsPaymentFileEntryList.isEmpty());

        } catch (FenixServiceException e) {
            fail("testReadExisting " + e.getMessage());
        }
    }
}