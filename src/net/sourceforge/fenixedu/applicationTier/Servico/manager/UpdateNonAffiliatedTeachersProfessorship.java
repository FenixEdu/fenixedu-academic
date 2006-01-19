package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class UpdateNonAffiliatedTeachersProfessorship extends Service {

    public void run(List<Integer> nonAffiliatedTeachersIds, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentObject().readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        for (final Integer nonAffiliatedTeachersId : nonAffiliatedTeachersIds) {
            final NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) sp
                    .getIPersistentObject().readByOID(NonAffiliatedTeacher.class,
                            nonAffiliatedTeachersId);
            executionCourse.removeNonAffiliatedTeachers(nonAffiliatedTeacher);
        }
    }

}
