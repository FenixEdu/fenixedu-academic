/*
 * Created on Dec 30, 2003
 *  
 */
package DataBeans.util;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExecutionCourse;
import Dominio.IDomainObject;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ClonerInfoObjectCachePerformanceTest extends ObjectFenixOJB
{

	private static SuportePersistenteOJB persistentSupport;
	private static ClonerInfoObjectCachePerformanceTest cacheTest;

	private static Calendar startTime;
	private static Calendar endTime;

	private static Class classToRead = ExecutionCourse.class;

	static {
		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
			cacheTest = new ClonerInfoObjectCachePerformanceTest();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ClonerInfoObjectCachePerformanceTest()
	{
		super();
	}

	public static void main(String[] args)
	{
		System.out.println("   ### Testing cache performance ###");
		try
		{
			persistentSupport.iniciarTransaccao();
			List infoExecutionCourses = readInfoExecutionCourses();

			startTime = Calendar.getInstance();
			for (int i = 0; i < /*infoExecutionCourses.size()*/ 2798; i++)
			{
				Cloner.get((IDomainObject) infoExecutionCourses.get(i));
			}
			endTime = Calendar.getInstance();
			System.out.println(
				"Cloning of "
					+ infoExecutionCourses.size()
					+ " objects took "
					+ cacheTest.calculateExecutionTime(startTime, endTime)
					+ " ms");

			startTime = Calendar.getInstance();
			for (int i = 0; i < infoExecutionCourses.size(); i++)
			{
				Cloner.get((IDomainObject) infoExecutionCourses.get(i));
			}
			endTime = Calendar.getInstance();
			System.out.println(
				"Cloning of "
					+ infoExecutionCourses.size()
					+ " objects took "
					+ cacheTest.calculateExecutionTime(startTime, endTime)
					+ " ms");

			persistentSupport.confirmarTransaccao();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static List readInfoExecutionCourses() throws ExcepcaoPersistencia
	{
		startTime = Calendar.getInstance();
		List objects = cacheTest.doTheRead();
		endTime = Calendar.getInstance();
		System.out.println(
			"Read a total of "
				+ objects.size()
				+ " "
				+ classToRead.getName()
				+ " in "
				+ cacheTest.calculateExecutionTime(startTime, endTime)
				+ " ms");
		return objects;
	}

	private List doTheRead() throws ExcepcaoPersistencia
	{
		return queryList(classToRead, new Criteria());
	}

	private long calculateExecutionTime(Calendar startTime, Calendar endTime)
	{
		return endTime.getTimeInMillis() - startTime.getTimeInMillis();
	}

}