/*
 * Created on 09/Sep/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
        
            
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



