package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Fernanda Quitério 5/Dez/2003
 *  
 */
abstract public class ReadDegreeCurricularPlanBaseService implements IServico
{
	protected List readActiveCurricularCourseScopes(
		IDegreeCurricularPlan degreeCurricularPlan,
		ISuportePersistente sp)
		throws FenixServiceException
	{
		//        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
		List infoActiveScopes = null;
		try
		{

			IPersistentCurricularCourseScope persistentCurricularCourseScope =
				sp.getIPersistentCurricularCourseScope();

			if (degreeCurricularPlan != null)
			{

				List allActiveScopes =
					persistentCurricularCourseScope
						.readActiveCurricularCourseScopesByDegreeCurricularPlan(
						degreeCurricularPlan);

				if (allActiveScopes != null && allActiveScopes.size() > 0)
				{
					infoActiveScopes = new ArrayList();

					CollectionUtils.collect(allActiveScopes, new Transformer()
					{
						public Object transform(Object input)
						{
							ICurricularCourseScope curricularCourseScope =
								(ICurricularCourseScope) input;
							return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
								curricularCourseScope);
						}
					}, infoActiveScopes);
				}
			}
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return infoActiveScopes;

	}
	
	//Read all curricular course scope of this year
	protected List readActiveCurricularCourseScopesInExecutionYear(
			IDegreeCurricularPlan degreeCurricularPlan,
			IExecutionYear executionYear,
			ISuportePersistente sp)
	throws FenixServiceException
	{
		List infoActiveScopes = null;
		try
		{

			IPersistentCurricularCourseScope persistentCurricularCourseScope =
			sp.getIPersistentCurricularCourseScope();

			if (degreeCurricularPlan != null)
			{

				List allActiveScopes =
				persistentCurricularCourseScope
				.readCurricularCourseScopesByCurricularCourseInExecutionYear(
						degreeCurricularPlan, executionYear);

				if (allActiveScopes != null && allActiveScopes.size() > 0)
				{
					infoActiveScopes = new ArrayList();

					CollectionUtils.collect(allActiveScopes, new Transformer()
							{
						public Object transform(Object input)
						{
							ICurricularCourseScope curricularCourseScope =
							(ICurricularCourseScope) input;
							return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
									curricularCourseScope);
						}
					}, infoActiveScopes);
				}
			}
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return infoActiveScopes;

	}
}
