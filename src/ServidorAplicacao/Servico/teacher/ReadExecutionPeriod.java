/*
 * Created on 24/Ago/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoExecutionPeriod;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
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

        	 ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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