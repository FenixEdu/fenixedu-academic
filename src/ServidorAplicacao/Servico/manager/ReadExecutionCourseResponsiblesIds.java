/*
 * Created on 23/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds implements IService {

    /**
     * Executes the service. Returns the current collection of ids of teachers.
     */

    public List run(Integer executionCourseId) throws FenixServiceException {

        List responsibles = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseId);
            responsibles = sp.getIPersistentResponsibleFor().readByExecutionCourse(executionCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (responsibles == null || responsibles.isEmpty())
            return new ArrayList();

        List ids = new ArrayList();
        Iterator iter = responsibles.iterator();

        while (iter.hasNext()) {
            ids.add(((IResponsibleFor) iter.next()).getTeacher().getIdInternal());
        }

        return ids;
    }
}