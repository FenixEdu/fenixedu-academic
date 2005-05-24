/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadExecutionCoursesByTeacherResponsibility implements IService {

    public List run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentResponsibleFor persistentResponsibleFor = persistentSuport
                .getIPersistentResponsibleFor();
        final List<IResponsibleFor> responsibilities = persistentResponsibleFor
                .readByTeacherNumber(teacherNumber);
        if (responsibilities != null) {
            for (final IResponsibleFor responsibleFor : responsibilities) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(responsibleFor
                        .getExecutionCourse()));
            }
        }
        return infoExecutionCourses;
    }
}