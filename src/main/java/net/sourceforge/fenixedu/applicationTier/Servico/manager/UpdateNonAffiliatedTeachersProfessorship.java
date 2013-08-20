package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class UpdateNonAffiliatedTeachersProfessorship {

    protected void run(List<String> nonAffiliatedTeachersIds, String executionCourseId) throws FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        List<NonAffiliatedTeacher> nonAffiliatedTeachersToRemove = new ArrayList<NonAffiliatedTeacher>();
        for (NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
            if (!nonAffiliatedTeachersIds.contains(nonAffiliatedTeacher.getExternalId())) {
                nonAffiliatedTeachersToRemove.add(nonAffiliatedTeacher);
            }
        }

        for (NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachersToRemove) {
            executionCourse.removeNonAffiliatedTeachers(nonAffiliatedTeacher);
        }
    }

    // Service Invokers migrated from Berserk

    private static final UpdateNonAffiliatedTeachersProfessorship serviceInstance =
            new UpdateNonAffiliatedTeachersProfessorship();

    @Service
    public static void runUpdateNonAffiliatedTeachersProfessorship(List<String> nonAffiliatedTeachersIds, String executionCourseId)
            throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(nonAffiliatedTeachersIds, executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(nonAffiliatedTeachersIds, executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}