/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Enrolment;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear extends
        InfoEnrolmentWithCourseAndDegree {
    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(enrolment
                    .getExecutionPeriod()));//with year
        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}