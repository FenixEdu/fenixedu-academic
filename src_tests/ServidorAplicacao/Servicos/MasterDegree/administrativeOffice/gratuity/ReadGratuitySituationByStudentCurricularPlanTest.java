/*
 * Created on 13/Jan/2004
 *  
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuitySituationByStudentCurricularPlanTest extends AdministrativeOfficeBaseTest
{

	public ReadGratuitySituationByStudentCurricularPlanTest(String name)
	{
		super(name);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuitySituationByStudentCurricularPlanDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadGratuitySituationByStudentCurricularPlan";
	}

	protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
	{
		Object[] args = { null };

		return args;
	}

	protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
	{
		Object[] argsReadStudentCurricularPlans = { new Integer(142), TipoCurso.MESTRADO_OBJ };
		//number, degree type
		List infoStudentCurricularPlans =
			(List) ServiceManagerServiceFactory.executeService(
				userView,
				"ReadStudentCurricularPlansByNumberAndDegreeTypeInMasterDegree",
				argsReadStudentCurricularPlans);

		Object[] argsReadGratuitySituationByStudentCurricularPlan =
			{((InfoStudentCurricularPlan) infoStudentCurricularPlans.get(0)).getIdInternal()};

		return argsReadGratuitySituationByStudentCurricularPlan;
	}

	public void testSucessReadGratuitySituationByStudentCurricularPlan()
	{

		try
		{
			Object[] argsReadStudentCurricularPlans = { new Integer(142), TipoCurso.MESTRADO_OBJ };
			//number, degree type
			List infoStudentCurricularPlans;

			infoStudentCurricularPlans =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentCurricularPlansByNumberAndDegreeTypeInMasterDegree",
					argsReadStudentCurricularPlans);

			Object[] argsReadGratuitySituationByStudentCurricularPlan =
				{((InfoStudentCurricularPlan) infoStudentCurricularPlans.get(0)).getIdInternal()};

			InfoGratuitySituation infoGratuitySituation =
				(InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadGratuitySituationByStudentCurricularPlan",
					argsReadGratuitySituationByStudentCurricularPlan);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessReadGratuitySituationByStudentCurricularPlan " + e.getMessage());
		}
	}
}
