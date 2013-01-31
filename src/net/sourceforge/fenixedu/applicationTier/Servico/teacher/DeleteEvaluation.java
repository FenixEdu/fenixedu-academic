/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;

public class DeleteEvaluation extends FenixService {

	/**
	 * @param Integer
	 *            executionCourseID used in filtering
	 *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
	 */
	public void run(Integer executionCourseID, Integer evaluationID) throws FenixServiceException {
		final Evaluation evaluation = rootDomainObject.readEvaluationByOID(evaluationID);
		if (evaluation == null) {
			throw new FenixServiceException("error.noEvaluation");
		}
		evaluation.delete();
	}

}
