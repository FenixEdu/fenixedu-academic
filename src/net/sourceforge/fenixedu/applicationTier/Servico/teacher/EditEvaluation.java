package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation extends FenixService {

	public Boolean run(final ExecutionCourse executionCourse, final MultiLanguageString evaluationMethod) {
		if (executionCourse.getEvaluationMethod() == null) {
			executionCourse.createEvaluationMethod(evaluationMethod);
		} else {
			executionCourse.getEvaluationMethod().edit(evaluationMethod);
		}
		return true;
	}
}