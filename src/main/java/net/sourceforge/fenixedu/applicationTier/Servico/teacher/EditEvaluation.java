package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation {

    protected Boolean run(final ExecutionCourse executionCourse, final MultiLanguageString evaluationMethod) {
        if (executionCourse.getEvaluationMethod() == null) {
            executionCourse.createEvaluationMethod(evaluationMethod);
        } else {
            executionCourse.getEvaluationMethod().edit(evaluationMethod);
        }
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditEvaluation serviceInstance = new EditEvaluation();

    @Atomic
    public static Boolean runEditEvaluation(ExecutionCourse executionCourse, MultiLanguageString evaluationMethod)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse.getExternalId());
        return serviceInstance.run(executionCourse, evaluationMethod);
    }

}