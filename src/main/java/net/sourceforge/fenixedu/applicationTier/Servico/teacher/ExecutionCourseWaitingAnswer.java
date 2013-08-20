/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseWaitingAnswer {

    protected Boolean run(String executionCourseID) throws FenixServiceException {
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        List<Grouping> groupings = executionCourse.getGroupings();
        for (final Grouping grouping : groupings) {
            final List<ExportGrouping> groupingExecutionCourses = grouping.getExportGroupings();
            for (final ExportGrouping groupingExecutionCourse : groupingExecutionCourses) {
                if (groupingExecutionCourse.getProposalState().getState().intValue() == ProposalState.EM_ESPERA) {
                    return true;
                }
            }
        }
        return false;
    }

    // Service Invokers migrated from Berserk

    private static final ExecutionCourseWaitingAnswer serviceInstance = new ExecutionCourseWaitingAnswer();

    @Service
    public static Boolean runExecutionCourseWaitingAnswer(String executionCourseID) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID);
    }

}