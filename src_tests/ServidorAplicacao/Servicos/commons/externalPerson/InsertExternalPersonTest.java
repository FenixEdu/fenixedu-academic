package ServidorAplicacao.Servicos.commons.externalPerson;

import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertExternalPersonTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public InsertExternalPersonTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/commons/externalPerson/testInsertExternalPersonDataSet.xml";
    }

    protected void setUp() {
        super.setUp();
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertExternalPerson";
    }

    public void testSucessfullInsert() {
        try {
            Object[] args = { "Some one external", "Some address", new Integer(23), "1234567890",
                    "981234567", "http://fenix-ashes.ist.utl.pt:8080", "suporte@dotist.utl.pt" };
            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/commons/externalPerson/testExpectedInsertExternalPersonSucessfullInsertDataSet.xml");

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSucessfullInsert " + ex.getMessage());
        }

    }

    public void testInsertExisting() {
        try {
            Object[] args = { "Xxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx", "XXXXXXXXXX",
                    new Integer(22), null, null, null, null };
            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), args);

            fail("testInsertExisting did not throw ExistingServiceException");

        } catch (ExistingServiceException ex) {
            //ok
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testInsertExisting " + ex.getMessage());
        }

    }

}