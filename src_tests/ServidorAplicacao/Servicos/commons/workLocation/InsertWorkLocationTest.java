package ServidorAplicacao.Servicos.commons.workLocation;

import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) 
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertWorkLocationTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public InsertWorkLocationTest(String testName) {
        super(testName);

        this.dataSetFilePath = "etc/datasets/servicos/commons/workLocation/testInsertWorkLocationDataSet.xml";

    }

    protected void setUp() {
        super.setUp();
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertWorkLocation";
    }

    public void testSuccessfullInsert() {
        try {

            Object[] args = { "Some name"};

            ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/commons/workLocation/testExpectedInsertWorkLocationSucessfullInsertDataSet.xml");

            //ok
            
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSuccessfullInsert " + ex.getMessage());
        }

    }

    public void testInsertExisting() {
        try {

            Object[] args = { "INSTITUTO DE TELECOMUNICAÇÕES"};

            ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            fail("testInsertExisting did not throw ExistingServiceException");
            
        } catch (ExistingServiceException ex) {
            //ok
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testInsertExisting " + ex.getMessage());
        }
    }

}