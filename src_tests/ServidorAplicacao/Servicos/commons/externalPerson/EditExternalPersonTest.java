package ServidorAplicacao.Servicos.commons.externalPerson;

import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) 
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditExternalPersonTest extends ServiceTestCase
{

    private String dataSetFilePath;

    /**
	 * @param testName
	 */
    public EditExternalPersonTest(String testName)
    {
        super(testName);
        this.dataSetFilePath =
            "etc/datasets/servicos/commons/externalPerson/testEditExternalPersonDataSet.xml";
    }

    protected void setUp()
    {
        super.setUp();
    }

    protected String getDataSetFilePath()
    {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditExternalPerson";
    }

    public void testSucessfullEdit()
    {
        try
        {
            
            Object[] args = {new Integer(24), "Some one external","Some address 2",new Integer(22),"1234567890","981234567","http://fenix-ashes.ist.utl.pt:8080","suporte@dotist.utl.pt" };
            ServiceManagerServiceFactory.executeService(
                    null,
                    getNameOfServiceToBeTested(),
                    args);
            
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/commons/externalPerson/testExpectedEditExternalPersonSucessfullEditDataSet.xml");

        } catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testSucessfullEdit " + ex.getMessage());
        }

    }
    
    public void testChangeToExisting()
    {
        try
        {
            Object[] args = {new Integer(24), "Xxxxxxxxxxxxxxxx Xxxxxxxxx Xxxxxxx Xxxxxxxxx","XXXXXXXXXX",new Integer(22),null,null,null,null };
            ServiceManagerServiceFactory.executeService(
                    null,
                    getNameOfServiceToBeTested(),
                    args);
            
            fail("testChangeToExisting did not throw ExistingServiceException");

        } catch(ExistingServiceException ex)
		{
			//ok
		} 
		catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testChangeToExisting " + ex.getMessage());
        }

    }
    
    

}
