/*
 * Created on Abr 1, 2004
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.exams.ReadAllBuildings;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadAllBuildingsTest extends ServiceTestCase
{

    /**
     * @param name
     */
    public ReadAllBuildingsTest(java.lang.String testName)
    {
        super(testName);
    }


    protected String getNameOfServiceToBeTested()
    {
        return "ReadAllBuildings";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/sop/testReadAllBuildingsExamsV4.xml";
    }



    // test successfull create exam 
	public void testSuccessfullReadAllBuildings()
	{
		ReadAllBuildings service = ReadAllBuildings.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List buildingNames = service.run();

			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			assertEquals(buildingNames.size(), 19);
			
		}
		catch (FenixServiceException ex)
		{
			//sp.cancelarTransaccao();
			fail("testSuccessfullReadAllBuildings - Fenix Service Exception " + ex);
		}
		catch (Exception ex)
		{
			//sp.cancelarTransaccao();			
			fail("testSuccessfullReadAllBuildings - Exception " + ex);
		}
	}
 

}
