/*
 * Created on 13/Jan/2004
 *  
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteGratuitySituatuinByIdTest extends AdministrativeOfficeBaseTest
{

	public DeleteGratuitySituatuinByIdTest(String name)
	{
		super(name);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testDeleteGratuitySituationByIdDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "DeleteGratuitySituationById";
	}

	protected Object[] getServiceArgumentsForNotAuthenticatedUser()
	{
		Object[] args = { new Integer(1)};

		return args;
	}

	protected Object[] getServiceArgumentsForNotAuthorizedUser()
	{
		Object[] argsEditGratuitySituationById = { new Integer(1)};

		return argsEditGratuitySituationById;
	}

	public void testSucessDeleteGratuitySituation()
	{
		Object[] args = { new Integer(1)};

		Boolean result = Boolean.FALSE;
		try
		{
			result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessDeleteGratuitySituation " + e.getMessage());
		}

		assertEquals(result, Boolean.TRUE);
		compareDataSet("tesDeleteGratuitySituationByIdConfirmationDataSet.xml");
	}

	public void testSucessDeleteGratuitySituationAlreadyUnExist()
	{
		Object[] args = { new Integer(2)};

		Boolean result = Boolean.FALSE;
		try
		{
			result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessDeleteGratuitySituationAlreadyUnExist " + e.getMessage());
		}

		assertEquals(result, Boolean.TRUE);
	}
}
