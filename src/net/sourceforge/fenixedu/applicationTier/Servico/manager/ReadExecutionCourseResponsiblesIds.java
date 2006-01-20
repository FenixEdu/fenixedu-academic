/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds extends Service {

    public List run(Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        
        ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
        
        List<Professorship> responsibles = executionCourse.responsibleFors();

        List<Integer> responsibleIDs = new ArrayList<Integer>();
        if (responsibles != null) {
            for (Professorship responsibleFor : responsibles) {
                responsibleIDs.add(responsibleFor.getTeacher().getIdInternal());
            }
        }
        return responsibleIDs;
    }
}