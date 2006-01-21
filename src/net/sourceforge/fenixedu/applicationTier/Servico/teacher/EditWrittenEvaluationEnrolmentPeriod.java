package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditWrittenEvaluationEnrolmentPeriod extends Service {

    public void run(Integer executionCourseID, Integer writtenEvaluationID, Date beginDate,
            Date endDate, Date beginTime, Date endTime) throws FenixServiceException,
            ExcepcaoPersistencia {
        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) persistentSupport
                .getIPersistentObject().readByOID(WrittenEvaluation.class, writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        writtenEvaluation.editEnrolmentPeriod(beginDate, endDate, beginTime, endTime);
    }
}