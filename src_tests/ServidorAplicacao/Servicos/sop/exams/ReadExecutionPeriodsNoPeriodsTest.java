/*
 * Created on 21/Abr/2004
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import ServidorAplicacao.Servico.commons.ReadExecutionPeriods;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana
 */
public class ReadExecutionPeriodsNoPeriodsTest extends ServiceTestCase
{
	/**
	 * @param name
	 */
	public ReadExecutionPeriodsNoPeriodsTest(java.lang.String testName)
	{
		super(testName);
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadExecutionPeriods";
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets_templates/servicos/sop/testEmptyExamsV4.xml";
	}

	public void testSuccessfullReadExecutionPeriodsEmptyList()
	{
		ReadExecutionPeriods service = ReadExecutionPeriods.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List executionPeriodsList = service.run();

			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

			assertEquals(executionPeriodsList.size(), 0);

		}
		catch (FenixServiceException ex)
		{
			fail("testSuccessfullReadExecutionPeriodsEmptyList" + ex);
		}
		catch (Exception ex)
		{
			fail("testSuccessfullReadExecutionPeriodsEmptyList error on compareDataSet" + ex);
		}
	}

}
