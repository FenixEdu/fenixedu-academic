/*
 * Created on 13/Jan/2004
 *  
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.ExemptionGratuityType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class EditGratuitySituationByIdTest extends AdministrativeOfficeBaseTest
{
	public EditGratuitySituationByIdTest(String name)
	{
		super(name);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testEditGratuitySituationByIdDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "EditGratuitySituationById";
	}

	protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
	{
		Object[] args = { new Integer(1)};

		return args;
	}

	protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
	{
		Object[] argsEditGratuitySituationById = { new Integer(1)};

		return argsEditGratuitySituationById;
	}

	private InfoGratuitySituation fillInfoGratuityValues(
		Integer id,
		Integer studentCurricularPlanId,
		Integer gratuityValuesId)
	{
		InfoGratuitySituation infoGratuitySituation = new InfoGratuitySituation();

		infoGratuitySituation.setIdInternal(id);
		infoGratuitySituation.setExemptionPercentage(new Integer(75));
		infoGratuitySituation.setExemptionType(ExemptionGratuityType.OTHER);
		infoGratuitySituation.setExemptionDescription("Outro motivo");

		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		infoStudentCurricularPlan.setIdInternal(studentCurricularPlanId);
		infoGratuitySituation.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		infoGratuityValues.setIdInternal(gratuityValuesId);
		infoGratuitySituation.setInfoGratuityValues(infoGratuityValues);

		return infoGratuitySituation;
	}

	public void testSucessEditGratuitySituation()
	{
		Object[] args = { fillInfoGratuityValues(new Integer(1), new Integer(8582), null)};

		InfoGratuitySituation infoGratuitySituation = null;
		try
		{
			infoGratuitySituation =
				(InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessEditGratuitySituation " + e.getMessage());
		}
		assertNotNull(infoGratuitySituation);
		assertEquals(infoGratuitySituation.getIdInternal(), new Integer(1));
		assertEquals(
			infoGratuitySituation.getInfoStudentCurricularPlan().getIdInternal(),
			new Integer(8582));
		assertNull(infoGratuitySituation.getInfoGratuityValues());
		assertEquals(infoGratuitySituation.getExemptionPercentage(), new Integer(75));
		assertEquals(new Integer(infoGratuitySituation.getExemptionType().getValue()), new Integer(10));
		assertEquals(infoGratuitySituation.getExemptionDescription(), "Outro motivo");

	}

	public void testSucessEditGratuitySituationWithNewOne()
	{

		Object[] args = { fillInfoGratuityValues(null, new Integer(8583), null)};

		InfoGratuitySituation infoGratuitySituation = null;
		try
		{
			infoGratuitySituation =
				(InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessEditGratuitySituationWithNewOne " + e.getMessage());
		}
		assertNotNull(infoGratuitySituation);
		assertEquals(
			infoGratuitySituation.getInfoStudentCurricularPlan().getIdInternal(),
			new Integer(8583));
		assertEquals(infoGratuitySituation.getInfoGratuityValues().getIdInternal(), new Integer(1));
		assertEquals(infoGratuitySituation.getExemptionPercentage(), new Integer(75));
		assertEquals(new Integer(infoGratuitySituation.getExemptionType().getValue()), new Integer(10));
		assertEquals(infoGratuitySituation.getExemptionDescription(), "Outro motivo");
	}
}
