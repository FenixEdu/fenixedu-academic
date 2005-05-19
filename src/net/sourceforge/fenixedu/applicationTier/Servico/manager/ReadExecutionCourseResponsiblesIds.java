/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds implements IService {

    public List run(Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        List<IResponsibleFor> responsibles = suportePersistente.getIPersistentResponsibleFor()
                .readByExecutionCourse(executionCourseId);

        List<Integer> responsibleIDs = new ArrayList<Integer>();
        if (responsibles != null) {
            for (IResponsibleFor responsibleFor : responsibles) {
                responsibleIDs.add(responsibleFor.getTeacher().getIdInternal());
            }
        }
        return responsibleIDs;
    }
}