package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditAdHocEvaluation extends Service {

    public void run(Integer executionCourseID, Integer adHocEvaluationID, String name, String description, GradeScale gradeScale)
	    throws FenixServiceException {

	AdHocEvaluation adHocEvaluation = (AdHocEvaluation) rootDomainObject.readEvaluationByOID(adHocEvaluationID);

	if (adHocEvaluation == null) {
	    throw new FenixServiceException("error.noEvaluation");
	}

	adHocEvaluation.edit(name, description, gradeScale);
    }

}
