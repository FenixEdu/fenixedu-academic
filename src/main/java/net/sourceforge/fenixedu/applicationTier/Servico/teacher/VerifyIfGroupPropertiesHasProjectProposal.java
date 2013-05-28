package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class VerifyIfGroupPropertiesHasProjectProposal {

    protected Boolean run(Integer executionCourseId, Integer groupPropertiesId) {
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        final Grouping grouping = AbstractDomainObject.fromExternalId(groupPropertiesId);
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