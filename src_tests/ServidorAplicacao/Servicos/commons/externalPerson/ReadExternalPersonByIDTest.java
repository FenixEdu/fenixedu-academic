package ServidorAplicacao.Servicos.commons.externalPerson;

import DataBeans.InfoExternalPerson;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *               (naat@mega.ist.utl.pt)
 */
public class ReadExternalPersonByIDTest extends ServiceTestCase
{

    private String dataSetFilePath;

    /**
	 * @param testName
	 */
    public ReadExternalPersonByIDTest(String testName)
    {
        super(testName);
        this.dataSetFilePath =
            "etc/datasets/servicos/commons/externalPerson/testReadExternalPersonByIDDataSet.xml";
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
        return "ReadExternalPersonByID";
    }

    public void testReadExistingExternalPerson()
    {
        try
        {
            Integer externalPersonID = new Integer(1);
            Object[] argsReadExternalPerson = { externalPersonID };

            InfoExternalPerson infoExternalPerson =
                (InfoExternalPerson) gestor.executar(
                    null,
                    getNameOfServiceToBeTested(),
                    argsReadExternalPerson);
            assertNotNull(infoExternalPerson);
            assertEquals(infoExternalPerson.getIdInternal(), externalPersonID);

        } catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testReadExistingExternalPerson " + ex.getMessage());
        }

    }

    public void testReadNonExistingExternalPerson()
    {
        try
        {
            Integer externalPersonID = new Integer(145);
            Object[] argsReadExternalPerson = { externalPersonID };

            gestor.executar(null, getNameOfServiceToBeTested(), argsReadExternalPerson);

            fail("testReadNonExistingExternalPerson did not throw NonExistingServiceException");

        } catch (NonExistingServiceException ee)
        {
            //ok

        } catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testReadNonExistingExternalPerson " + ex.getMessage());
        }
    }

}
