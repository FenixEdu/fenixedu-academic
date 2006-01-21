package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

public class InsertProfessorShipNonAffiliatedTeacher extends Service {

    public void run(Integer nonAffiliatedTeacherID, Integer executionCourseID) throws Exception {

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        final NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) persistentObject.
        		readByOID(NonAffiliatedTeacher.class, nonAffiliatedTeacherID);
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
