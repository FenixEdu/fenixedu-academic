package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class FixSibsEntryByIDTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public FixSibsEntryByIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testFixSibsEntryByIDDataSet.xml";

    }

    protected String getNameOfServiceToBeTested() {
        return "FixSibsEntryByID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(1095) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser()
            throws FenixServiceException {
        Object[] args = { new Integer(1095) };

        return args;
    }

    public void testSucessfull() {

        try {
            Integer sibsFileEntryID = new Integer(1095);

            Object[] args = { sibsFileEntryID };

            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testExpectedFixSibsEntryByIDSucessfullDataSet.xml");

        } catch (FenixServiceException e) {
            fail("testSucessfull " + e.getMessage());
        }

    }

}