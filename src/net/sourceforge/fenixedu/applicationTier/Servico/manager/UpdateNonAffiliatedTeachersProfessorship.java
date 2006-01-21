package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class UpdateNonAffiliatedTeachersProfessorship extends Service {

    public void run(List<Integer> nonAffiliatedTeachersIds, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        for (final Integer nonAffiliatedTeachersId : nonAffiliatedTeachersIds) {
            final NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) persistentObject
            		.readByOID(NonAffiliatedTeacher.class,
                            nonAffiliatedTeachersId);
            executionCourse.removeNonAffiliatedTeachers(nonAffiliatedTeacher);
        }
    }

}
