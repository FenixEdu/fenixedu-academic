/*
 * Created on 8/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByExecutionPeriod implements IServico
{

	private static ReadExecutionCoursesByExecutionPeriod service =
		new ReadExecutionCoursesByExecutionPeriod();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadExecutionCoursesByExecutionPeriod getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadExecutionCoursesByExecutionPeriod()
	{
	}

	/**
	 * Service name
	 */
	public final String getNome()
	{
		return "ReadExecutionCoursesByExecutionPeriod";
	}

	/**
	 * Executes the service. Returns the current collection of infoExecutionCourses.
	 */
	public List run(Integer executionPeriodId) throws FenixServiceException
	{
		List allExecutionCoursesFromExecutionPeriod = null;
		List allExecutionCourses = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriodToRead = new ExecutionPeriod();
			executionPeriodToRead.setIdInternal(executionPeriodId);

			IExecutionPeriod executionPeriod =
				(IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriodToRead, false);

			if (executionPeriod == null)
			{
				throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
			}
			allExecutionCoursesFromExecutionPeriod =
				persistentExecutionCourse.readByExecutionPeriod(executionPeriod);

			if (allExecutionCoursesFromExecutionPeriod == null
				|| allExecutionCoursesFromExecutionPeriod.isEmpty())
			{
				return allExecutionCoursesFromExecutionPeriod;
			}
			InfoExecutionCourse infoExecutionCourse = null;
			allExecutionCourses = new ArrayList(allExecutionCoursesFromExecutionPeriod.size());
			Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
			while (iter.hasNext())
			{
				IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
				Boolean hasSite = persistentExecutionCourse.readSite(executionCourse.getIdInternal());
				infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
				infoExecutionCourse.setHasSite(hasSite);
				allExecutionCourses.add(infoExecutionCourse);
			}
		}
		catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return allExecutionCourses;
	}
}