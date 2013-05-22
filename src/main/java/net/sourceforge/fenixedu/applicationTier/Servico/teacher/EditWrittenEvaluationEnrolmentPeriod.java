package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import pt.ist.fenixWebFramework.services.Service;

public class EditWrittenEvaluationEnrolmentPeriod extends FenixService {

    protected void run(Integer executionCourseID, Integer writtenEvaluationID, Date beginDate, Date endDate, Date beginTime,
            Date endTime) throws FenixServiceException {
        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        writtenEvaluation.editEnrolmentPeriod(beginDate, endDate, beginTime, endTime);
    }

    // Service Invokers migrated from Berserk

    private static final EditWrittenEvaluationEnrolmentPeriod serviceInstance = new EditWrittenEvaluationEnrolmentPeriod();

    @Service
    public static void runEditWrittenEvaluationEnrolmentPeriod(Integer executionCourseID, Integer writtenEvaluationID,
            Date beginDate, Date endDate, Date beginTime, Date endTime) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, writtenEvaluationID, beginDate, endDate, beginTime, endTime);
    }

}