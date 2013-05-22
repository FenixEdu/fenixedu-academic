/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Evaluation;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteEvaluation extends FenixService {

    /**
     * @param Integer
     *            executionCourseID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    protected void run(Integer executionCourseID, Integer evaluationID) throws FenixServiceException {
        final Evaluation evaluation = rootDomainObject.readEvaluationByOID(evaluationID);
        if (evaluation == null) {
            throw new FenixServiceException("error.noEvaluation");
        }
        evaluation.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteEvaluation serviceInstance = new DeleteEvaluation();

    @Service
    public static void runDeleteEvaluation(Integer executionCourseID, Integer evaluationID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, evaluationID);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, evaluationID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}