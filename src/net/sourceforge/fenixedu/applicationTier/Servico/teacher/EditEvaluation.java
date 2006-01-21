package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation extends Service {

    public boolean run(Integer infoExecutionCourseCode, Integer infoEvaluationMethodCode,
            InfoEvaluationMethod infoEvaluationMethod) throws FenixServiceException,
            ExcepcaoPersistencia {
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse(); 
        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, infoExecutionCourseCode);

        if (executionCourse.getEvaluationMethod() == null) { // Create a new one
            executionCourse.createEvaluationMethod(infoEvaluationMethod.getEvaluationElements(),
                    infoEvaluationMethod.getEvaluationElementsEn());
        } else { // Edit existent
            executionCourse.getEvaluationMethod().edit(infoEvaluationMethod.getEvaluationElements(),
                    infoEvaluationMethod.getEvaluationElementsEn());
        }
        return true;
    }
}