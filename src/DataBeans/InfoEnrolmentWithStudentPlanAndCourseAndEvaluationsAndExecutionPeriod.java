package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;

/**
 * @author Fernanda Quitério 20/Jul/2004
 */
public class InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod extends
        InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod {

    public void copyFromDomain(IEnrollment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoEvaluations(copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolment
                    .getEvaluations()));
        }
    }

    /**
     * @param list
     * @return
     */
    private static List copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(List list) {
        List infoEnrolmentEvaluations = null;
        if (list != null) {
            infoEnrolmentEvaluations = (List) CollectionUtils.collect(list, new Transformer() {

                public Object transform(Object arg0) {

                    return InfoEnrolmentEvaluation.newInfoFromDomain((IEnrolmentEvaluation) arg0);
                }

            });
        }
        return infoEnrolmentEvaluations;
    }

    public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
        InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}