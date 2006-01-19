package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class InsertProfessorShipNonAffiliatedTeacher extends Service {

    public void run(Integer nonAffiliatedTeacherID, Integer executionCourseID) throws Exception {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentObject().readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        final NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) sp
                .getIPersistentObject().readByOID(NonAffiliatedTeacher.class, nonAffiliatedTeacherID);
        if (nonAffiliatedTeacher == null) {
            throw new NonExistingServiceException("message.non.existing.nonAffiliatedTeacher", null);
        }

        if (nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
            throw new ExistingServiceException();
        } else {
            nonAffiliatedTeacher.addExecutionCourses(executionCourse);
        }
    }

}
