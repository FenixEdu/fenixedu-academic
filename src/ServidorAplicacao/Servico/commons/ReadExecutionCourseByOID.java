/*
 * Created on 2003/07/29
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadExecutionCourseByOID implements IService {

    /**
     *  
     */
    public ReadExecutionCourseByOID() {

    }

    public InfoExecutionCourse run(Integer oid) throws FenixServiceException {

        InfoExecutionCourse result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentObject.readByOID(
                    ExecutionCourse.class, oid);
            if (executionCourse != null) {
                result = (InfoExecutionCourse) Cloner.get(executionCourse);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}