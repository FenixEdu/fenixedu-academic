package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation extends Service {

    public boolean run(final ExecutionCourse executionCourse, final MultiLanguageString evaluationMethod) {
        if (executionCourse.getEvaluationMethod() == null) {
            executionCourse.createEvaluationMethod(evaluationMethod);
        } else {
            executionCourse.getEvaluationMethod().edit(evaluationMethod);
        }
        return true;
    }
}