package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */

public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear implements IServico
{

	private static ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear _servico =
		new ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear();

	/**
	 * The actor of this class.
	 */

	private ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear()
	{

	}

	/**
	 * Returns Service Name
	 */
	public String getNome()
	{
		return "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear";
	}

	/**
	 * Returns the _servico.
	 * 
	 * @return ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear
	 */
	public static ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear getService()
	{
		return _servico;
	}

	public Object run(Integer executionDegreeId, Integer executionPeriodId, Integer curricularYear)
		throws FenixServiceException
	{

		List infoExecutionCourseList = new ArrayList();
		try
		{
			List executionCourseList = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

			if (executionPeriodId == null)
			{
				throw new FenixServiceException("nullExecutionPeriodId");
			}

			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			executionPeriod.setIdInternal(executionPeriodId);
			executionPeriod =
				(IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);

			if (executionDegreeId == null && curricularYear == null)
			{
								executionCourseList =
				 executionCourseDAO.readByExecutionPeriodWithNoCurricularCourses(executionPeriod);

//				executionCourseList = new ArrayList();
//				Integer number1 = new Integer(36457);
//				Integer number2 = new Integer(36407);
//				Integer number3 = new Integer(36412);
//				Integer number4 = new Integer(36681);
//				Integer number5 = new Integer(37358);
//				Integer number6 = new Integer(37260);
//				
//				IExecutionCourse executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number1);
//				executionCourse =
//					(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
//
//				executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number2);
//				executionCourse =
//					(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
//				executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number3);
//				executionCourse =
//					(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
//				executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number4);
//				executionCourse =
//				(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
//				executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number5);
//				executionCourse =
//				(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
//				executionCourse = new ExecutionCourse();
//				executionCourse.setIdInternal(number6);
//				executionCourse =
//				(IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
//				if (executionCourse != null)
//				{
//					executionCourseList.add(executionCourse);
//				}
			}
			else
			{
				ICursoExecucao executionDegree = new CursoExecucao();
				executionDegree.setIdInternal(executionDegreeId);
				executionDegree =
					(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);

				executionCourseList =
					executionCourseDAO.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
						curricularYear,
						executionPeriod,
						executionDegree);
			}

			CollectionUtils.collect(executionCourseList, new Transformer()
			{
				public Object transform(Object input)
				{
					IExecutionCourse executionCourse = (IExecutionCourse) input;
					return Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
				}
			}, infoExecutionCourseList);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
		}

		return infoExecutionCourseList;
	}
}