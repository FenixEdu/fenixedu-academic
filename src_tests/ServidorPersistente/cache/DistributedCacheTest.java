/*
 * Created on Dec 28, 2003
 *  
 */
package ServidorPersistente.cache;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class DistributedCacheTest extends ObjectFenixOJB
{

	private static SuportePersistenteOJB persistentSupport;
	private static DistributedCacheTest cacheTest;

	private static Calendar startTime;
	private static Calendar endTime;

	static {
		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
			cacheTest = new DistributedCacheTest();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DistributedCacheTest()
	{
		super();
	}

	public static void main(String[] args)
	{
		System.out.println("   ### Testing distributed cache ###");
		try
		{
			readAlotOfObjects();
			readAlotOfObjects();
			editSomeObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static void editSomeObject() throws ExcepcaoPersistencia
	{
		System.out.println("Obtainning object to edit");
		persistentSupport.iniciarTransaccao();
		List executionPeriods = cacheTest.readAllExecutionPeriods();
		ExecutionPeriod executionPeriod = (ExecutionPeriod) executionPeriods.get(0);
		cacheTest.simpleLockWrite(executionPeriod);
		executionPeriod.setBeginDate(Calendar.getInstance().getTime());
		persistentSupport.confirmarTransaccao();
		System.out.println("Altered one column of retieved object.");
	}

	private static void readAlotOfObjects() throws ExcepcaoPersistencia
	{
		persistentSupport.iniciarTransaccao();
		System.out.println("Reading all execution courses...");
		startTime = Calendar.getInstance();
		List executionCourses = cacheTest.readAllExecutionPeriods();
		endTime = Calendar.getInstance();
		System.out.println(
			"Read a total of "
				+ executionCourses.size()
				+ " execution courses in "
				+ cacheTest.calculateServiceExecutionTime(startTime, endTime)
				+ " miliseconds");
		persistentSupport.confirmarTransaccao();
	}

	private List readAllExecutionPeriods() throws ExcepcaoPersistencia
	{
		return queryList(ExecutionPeriod.class, new Criteria());
	}

	private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime)
	{
		return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
	}

}