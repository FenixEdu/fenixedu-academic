/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author joaosa & rmalo
 *  
 */
public class ExecutionCourseHasProposals implements IServico {

    private static ExecutionCourseHasProposals service = new ExecutionCourseHasProposals();

    /**
     * The singleton access method of this class.
     */
    public static ExecutionCourseHasProposals getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ExecutionCourseHasProposals() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ExecutionCourseHasProposals";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode) throws FenixServiceException {

        boolean result = false;
        IPersistentExecutionCourse persistentExecutionCourse = null;
         
        try {

            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
            
            persistentExecutionCourse = ps.getIPersistentExecutionCourse();
            
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
            		ExecutionCourse.class, executionCourseCode);
            
            result = executionCourse.hasProposals();
            
                        
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        
        return result;

    }
}



