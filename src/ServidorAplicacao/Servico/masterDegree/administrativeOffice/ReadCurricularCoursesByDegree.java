package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 01/07/2003
 *  
 */
public class ReadCurricularCoursesByDegree implements IServico
{

	private static ReadCurricularCoursesByDegree servico = new ReadCurricularCoursesByDegree();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadCurricularCoursesByDegree getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 */
	private ReadCurricularCoursesByDegree()
	{
	}

	/**
	 * Returns The Service Name
	 */

	public final String getNome()
	{
		return "ReadCurricularCoursesByDegree";
	}

	public List run(String executionYearString, String degreeName) throws FenixServiceException
	{
		List infoCurricularCourses = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionYear executionYear =
				sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);

			// Read degree
			ICursoExecucao cursoExecucao =
				sp.getICursoExecucaoPersistente().readByDegreeCurricularPlanNameAndExecutionYear(
					degreeName,
					executionYear);

			if (cursoExecucao == null 
					|| cursoExecucao.getCurricularPlan() == null 
					|| cursoExecucao.getCurricularPlan().getCurricularCourses() == null
				|| cursoExecucao.getCurricularPlan().getCurricularCourses().size() == 0)
			{
				throw new NonExistingServiceException();
			}

			infoCurricularCourses = new ArrayList();
			ListIterator iterator =
				cursoExecucao.getCurricularPlan().getCurricularCourses().listIterator();
			while (iterator.hasNext())
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

				InfoCurricularCourse infoCurricularCourse =
					Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourse
					.setInfoScopes(
						(List) CollectionUtils
						.collect(curricularCourse.getScopes(), new Transformer()
				{

					public Object transform(Object arg0)
					{
						ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
						return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
							curricularCourseScope);
					}
				}));

				infoCurricularCourses.add(infoCurricularCourse);
			}

		}
		catch (ExcepcaoPersistencia ex)
		{
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoCurricularCourses;
	}
	
	// comment by Fernanda Quitério
	//TODO: I've checked that this code is not used. Maybe it should be deleted. 
//	public List run(String executionYearString, String degreeName,String curricularPlanCode) throws FenixServiceException
//		{
//			List infoCurricularCourses = null;
//			try
//			{
//				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//
//				IExecutionYear executionYear =
//					sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);
//
//				// Read degree
//				ICursoExecucao cursoExecucao =
//					sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
//						degreeName,
//				curricularPlanCode,
//					executionYear);
//
//				if (cursoExecucao.getCurricularPlan().getCurricularCourses() == null
//					|| cursoExecucao.getCurricularPlan().getCurricularCourses().size() == 0)
//				{
//					throw new NonExistingServiceException();
//				}
//
//				infoCurricularCourses = new ArrayList();
//				ListIterator iterator =
//					cursoExecucao.getCurricularPlan().getCurricularCourses().listIterator();
//				while (iterator.hasNext())
//				{
//					ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
//					InfoCurricularCourse infoCurricularCourse =
//						Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
//					infoCurricularCourse
//						.setInfoScopes(
//							(List) CollectionUtils
//							.collect(curricularCourse.getScopes(), new Transformer()
//					{
//
//						public Object transform(Object arg0)
//						{
//							ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
//							return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
//								curricularCourseScope);
//						}
//					}));
//
//					infoCurricularCourses.add(infoCurricularCourse);
//				}
//
//			}
//			catch (ExcepcaoPersistencia ex)
//			{
//				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
//				newEx.fillInStackTrace();
//				throw newEx;
//			}
//
//			return infoCurricularCourses;
//		}
}
