package ServidorAplicacao.Servico.coordinator;

import java.util.List;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 */
public class ReadActiveDegreeCurricularPlanByExecutionDegreeCode
	extends ReadDegreeCurricularPlanBaseService
{

	private static ReadActiveDegreeCurricularPlanByExecutionDegreeCode service =
		new ReadActiveDegreeCurricularPlanByExecutionDegreeCode();

	public static ReadActiveDegreeCurricularPlanByExecutionDegreeCode getService()
	{

		return service;
	}

	private ReadActiveDegreeCurricularPlanByExecutionDegreeCode()
	{

	}

	public final String getNome()
	{

		return "ReadActiveDegreeCurricularPlanByExecutionDegreeCode";
	}

	public List run(Integer executionDegreeCode) throws FenixServiceException
	{

		ISuportePersistente sp;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
			throw new FenixServiceException("Problems on database!");
		}
		ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
		if (executionDegreeCode == null)
		{
			throw new FenixServiceException("nullDegree");
		}

		ICursoExecucao executionDegree = new CursoExecucao();
		executionDegree.setIdInternal(executionDegreeCode);
		executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);

		if (executionDegree == null)
		{
			throw new NonExistingServiceException();
		}

		IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();
		return super.readActiveCurricularCourseScopes(degreeCurricularPlan, sp);

	}
}