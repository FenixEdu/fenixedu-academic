package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadEvaluations extends Service {

    public List run(Integer executionCourseCode) throws FenixServiceException{
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
        if (executionCourse == null) {
            throw new NonExistingServiceException();
        }

        List<Evaluation> evaluations = executionCourse.getAssociatedEvaluations();
        List<InfoEvaluation> infoEvaluations = new ArrayList<InfoEvaluation>(evaluations.size());
        for (Evaluation evaluation : evaluations) {
            infoEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
        }

        return infoEvaluations;
    }

}
