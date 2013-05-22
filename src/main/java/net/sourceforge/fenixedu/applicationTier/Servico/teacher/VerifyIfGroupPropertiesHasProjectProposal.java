package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixWebFramework.services.Service;

public class VerifyIfGroupPropertiesHasProjectProposal extends FenixService {

    protected Boolean run(Integer executionCourseId, Integer groupPropertiesId) {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        final Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesId);
        return executionCourse.hasExportGrouping(grouping);
    }

    // Service Invokers migrated from Berserk

    private static final VerifyIfGroupPropertiesHasProjectProposal serviceInstance =
            new VerifyIfGroupPropertiesHasProjectProposal();

    @Service
    public static Boolean runVerifyIfGroupPropertiesHasProjectProposal(Integer executionCourseId, Integer groupPropertiesId)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId);
    }

}