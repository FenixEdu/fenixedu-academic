package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class VerifyIfGroupPropertiesHasProjectProposal {

    protected Boolean run(String executionCourseId, String groupPropertiesId) {
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        final Grouping grouping = FenixFramework.getDomainObject(groupPropertiesId);
        return executionCourse.hasExportGrouping(grouping);
    }

    // Service Invokers migrated from Berserk

    private static final VerifyIfGroupPropertiesHasProjectProposal serviceInstance =
            new VerifyIfGroupPropertiesHasProjectProposal();

    @Atomic
    public static Boolean runVerifyIfGroupPropertiesHasProjectProposal(String executionCourseId, String groupPropertiesId)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId);
    }

}