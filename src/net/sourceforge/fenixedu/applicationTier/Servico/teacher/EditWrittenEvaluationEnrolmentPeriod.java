package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class EditWrittenEvaluationEnrolmentPeriod extends Service {

    public void run(Integer executionCourseID, Integer writtenEvaluationID, Date beginDate, Date endDate, Date beginTime,
	    Date endTime) throws FenixServiceException {
	final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationID);
	if (writtenEvaluation == null) {
	    throw new FenixServiceException("error.noWrittenEvaluation");
	}
	writtenEvaluation.editEnrolmentPeriod(beginDate, endDate, beginTime, endTime);
    }
}