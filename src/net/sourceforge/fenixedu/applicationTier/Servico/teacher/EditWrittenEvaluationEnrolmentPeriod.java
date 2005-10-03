package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditWrittenEvaluationEnrolmentPeriod implements IService {

    public void run(Integer executionCourseID, Integer writtenEvaluationID, Calendar beginDate,
            Calendar endDate, Calendar beginTime, Calendar endTime) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) persistentSupport
                .getIPersistentEvaluation().readByOID(WrittenEvaluation.class, writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        writtenEvaluation.editEnrolmentPeriod(beginDate.getTime(), endDate.getTime(), beginTime
                .getTime(), endTime.getTime());
    }
}