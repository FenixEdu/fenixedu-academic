/*
 * Created on Dec 28, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.cache;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.cache.logging.CacheLog;

/**
 * @author Luis Cruz
 *  
 */
public class DistributedCacheConcurrencyTest extends PersistentObjectOJB {

    private static SuportePersistenteOJB persistentSupport;

    private static DistributedCacheConcurrencyTest cacheTest;

    private static Calendar startTime;

    private static Calendar endTime;

    static {
        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            cacheTest = new DistributedCacheConcurrencyTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DistributedCacheConcurrencyTest() {
        super();
    }

    public static void main(String[] args) {
        System.out.println("   ### Testing distributed cache concurrency test ###");
        try {
            readAlotOfObjects();
            readCacheStatistics();
            System.out.print("Press <return> to continue");
            System.in.read();
            readAlotOfObjects();
            readCacheStatistics();
            System.out.print("Press <return> to continue");
            System.in.read();
            editSomeObject();
            readCacheStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  
     */
    private static void readCacheStatistics() {
        System.out.println("   number puts               : " + CacheLog.getNumberPuts());
        System.out.println("   number removes            : " + CacheLog.getNumberRemoves());
        System.out.println("   number successful lookups : " + CacheLog.getNumberSuccessfulLookUps());
        System.out.println("   number failed lookups     : " + CacheLog.getNumberFailedLookUps());
        System.out.println("   number clears             : " + CacheLog.getNumberClears());
    }

    /**
     *  
     */
    private static void editSomeObject() throws ExcepcaoPersistencia {
        System.out.println("Obtainning object to edit");
        persistentSupport.iniciarTransaccao();
        List executionPeriods = cacheTest.readAllExecutionPeriods();
        ExecutionPeriod executionPeriod = (ExecutionPeriod) executionPeriods.get(0);
        cacheTest.simpleLockWrite(executionPeriod);
        executionPeriod.setBeginDate(Calendar.getInstance().getTime());
        persistentSupport.confirmarTransaccao();
        System.out.println("Altered one column of retieved object.");
    }

    private static void readAlotOfObjects() throws ExcepcaoPersistencia {
        persistentSupport.iniciarTransaccao();
        System.out.println("Reading all execution courses...");
        startTime = Calendar.getInstance();
        List executionCourses = cacheTest.readAllExecutionPeriods();
        endTime = Calendar.getInstance();
        System.out.println("Read a total of " + executionCourses.size() + " execution courses in "
                + cacheTest.calculateServiceExecutionTime(startTime, endTime) + " miliseconds");
        persistentSupport.confirmarTransaccao();
    }

    private List readAllExecutionPeriods() throws ExcepcaoPersistencia {
        return queryList(ExecutionPeriod.class, new Criteria());
    }

    private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime) {
        return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
    }

}