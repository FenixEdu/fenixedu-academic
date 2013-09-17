package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditWrittenEvaluationEnrolmentPeriod {

    protected void run(String executionCourseID, String writtenEvaluationID, Date beginDate, Date endDate, Date beginTime,
            Date endTime) throws FenixServiceException {
        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        writtenEvaluation.editEnrolmentPeriod(beginDate, endDate, beginTime, endTime);
    }

    // Service Invokers migrated from Berserk

    private static final EditWrittenEvaluationEnrolmentPeriod serviceInstance = new EditWrittenEvaluationEnrolmentPeriod();

    @Atomic
    public static void runEditWrittenEvaluationEnrolmentPeriod(String executionCourseID, String writtenEvaluationID,
            Date beginDate, Date endDate, Date beginTime, Date endTime) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, writtenEvaluationID, beginDate, endDate, beginTime, endTime);
    }

}