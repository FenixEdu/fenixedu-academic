/*
 * Created on Jul 29, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseGroup;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class AddCurricularCoursesToGroup implements IService {

    /**
     *  
     */
    public AddCurricularCoursesToGroup() {
    }

    public void run(Integer groupId, Integer[] courseIds) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId, true);
            if (curricularCourseGroup.getCurricularCourses() == null) {
                curricularCourseGroup.setCurricularCourses(new ArrayList());
            }
            for (int i = 0; i < courseIds.length; i++) {
                Integer courseId = courseIds[i];
                ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                        .readByOID(CurricularCourse.class, courseId);
                curricularCourseGroup.getCurricularCourses().add(curricularCourse);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}