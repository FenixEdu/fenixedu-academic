/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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