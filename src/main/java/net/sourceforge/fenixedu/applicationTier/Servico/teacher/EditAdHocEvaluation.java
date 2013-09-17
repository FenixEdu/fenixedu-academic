package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditAdHocEvaluation {

    @Atomic
    public static void run(String executionCourseID, String adHocEvaluationID, String name, String description,
            GradeScale gradeScale) throws FenixServiceException {

        AdHocEvaluation adHocEvaluation = (AdHocEvaluation) FenixFramework.getDomainObject(adHocEvaluationID);

        if (adHocEvaluation == null) {
            throw new FenixServiceException("error.noEvaluation");
        }

        adHocEvaluation.edit(name, description, gradeScale);
    }

}