/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author joaosa & rmalo
 *
 */

public class ReadExecutionPeriod implements IServico {

    private static ReadExecutionPeriod service = new ReadExecutionPeriod();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionPeriod getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadExecutionPeriod() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadExecutionPeriod";
    }

    /**
     * Executes the service.
     */

    public InfoExecutionPeriod run (Integer executionCourseCode) throws FenixServiceException {

        
        
        IPersistentExecutionCourse persistentExecutionCourse = null;
        IExecutionPeriod executionPeriod = null;

        try {

        	 ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentExecutionCourse = sp.getIPersistentExecutionCourse();
    		IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
              .readByOID(ExecutionCourse.class, executionCourseCode);
    		
    		executionPeriod =  executionCourse.getExecutionPeriod();
    		
    		
    		
    		
            
            

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
    }
}