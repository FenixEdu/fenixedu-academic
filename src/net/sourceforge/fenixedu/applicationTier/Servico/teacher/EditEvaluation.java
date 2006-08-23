package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation extends Service {

    public boolean run(Integer infoExecutionCourseCode, Integer infoEvaluationMethodCode,
            InfoEvaluationMethod infoEvaluationMethod) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourseCode);

        if (executionCourse.getEvaluationMethod() == null) { // Create a new one
            executionCourse.createEvaluationMethod(infoEvaluationMethod.getEvaluationElements());
        } else { // Edit existent
            executionCourse.getEvaluationMethod().edit(infoEvaluationMethod.getEvaluationElements());
        }
        return true;
    }
}