/*
 * Created on 09/Sep/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
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
public class ExecutionCourseWaitingAnswer implements IServico {

    private static ExecutionCourseWaitingAnswer service = new ExecutionCourseWaitingAnswer();

    /**
     * The singleton access method of this class.
     */
    public static ExecutionCourseWaitingAnswer getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ExecutionCourseWaitingAnswer() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ExecutionCourseWaitingAnswer";
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
            
            
            List groupPropertiesList = executionCourse.getGroupProperties();
            Iterator iterGroupPropertiesList = groupPropertiesList.iterator();
            while(iterGroupPropertiesList.hasNext() && !result){
            	IGroupProperties groupProperties = (IGroupProperties)iterGroupPropertiesList.next();
            	List groupPropertiesExecutionCourseList = groupProperties.getGroupPropertiesExecutionCourse();
            	Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
            	while(iterGroupPropertiesExecutionCourseList.hasNext() && !result){
            		IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse)iterGroupPropertiesExecutionCourseList.next();
            		if(groupPropertiesExecutionCourse.getProposalState().getState().intValue()==3){
            			result=true;
            		}
            	}
            }
            
                        
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        
        return result;

    }
}
