package ServidorAplicacao.Servico.publico;

import java.util.List;

import DataBeans.InfoDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionPeriod;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 *  
 */
public class ReadActiveDegreeCurricularPlanByID extends ReadDegreeCurricularPlanBaseService
{

	private static ReadActiveDegreeCurricularPlanByID service = new ReadActiveDegreeCurricularPlanByID();

	public static ReadActiveDegreeCurricularPlanByID getService()
	{

		return service;
	}

	private ReadActiveDegreeCurricularPlanByID()
	{

	}

	public final String getNome()
	{

		return "ReadActiveDegreeCurricularPlanByID";
	}

	public List run(Integer degreeCurricularPlanCode, Integer executionPeriodCode)
		throws FenixServiceException
	{
		if (degreeCurricularPlanCode == null)
		{
			throw new FenixServiceException("nullDegree");
		}

		ISuportePersistente sp;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
			throw new FenixServiceException("Problems on database!");
		}

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
			sp.getIPersistentDegreeCurricularPlan();

		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
		degreeCurricularPlan.setIdInternal(degreeCurricularPlanCode);
		degreeCurricularPlan =
			(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
				degreeCurricularPlan,
				false);
		if (degreeCurricularPlan == null)
		{
			throw new FenixServiceException("nullDegree");
		}

		if (executionPeriodCode == null)
		{
			return super.readActiveCurricularCourseScopes(degreeCurricularPlan, sp);
		}
		else
		{
			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			executionPeriod.setIdInternal(executionPeriodCode);
			
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);
			if(executionPeriod == null || executionPeriod.getExecutionYear() == null)
			{
				throw new FenixServiceException("nullDegree");
			}
			
			return super.readActiveCurricularCourseScopesInExecutionYear(degreeCurricularPlan, executionPeriod.getExecutionYear(), sp);
		}
	}
}