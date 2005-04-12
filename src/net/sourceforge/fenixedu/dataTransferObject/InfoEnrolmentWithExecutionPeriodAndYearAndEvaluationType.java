/*
 * Created on Dec 21, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolment;

/**
 * @author André Fernandes / João Brito
 */
public class InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType extends
        InfoEnrolmentWithExecutionPeriodAndYear
{
    public void copyFromDomain(IEnrolment enrolment) {
        super.copyFromDomain(enrolment);
    }

    public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}
