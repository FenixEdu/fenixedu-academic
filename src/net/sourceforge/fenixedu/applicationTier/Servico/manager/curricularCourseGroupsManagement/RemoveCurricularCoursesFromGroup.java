/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class RemoveCurricularCoursesFromGroup implements IService {

    /**
     *  
     */
    public RemoveCurricularCoursesFromGroup() {

    }

    public void run(Integer groupId, Integer[] courseIds) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId, true);

            curricularCourseGroup.getCurricularCourses().clear();
            for (int i = 0; i < courseIds.length; i++) {
                Integer courseId = courseIds[i];
                ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                        .readByOID(CurricularCourse.class, courseId);
                curricularCourseGroup.addCurricularCourses(curricularCourse);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}
