/*
 * Created on 8/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

import java.util.Date;

/**
 * @author jpvl
 */
public class OutOfCurricularCourseEnrolmentPeriod extends OutOfPeriodException {
    /**
     * @param messageKey
     * @param startDate
     * @param endDate
     */
    public OutOfCurricularCourseEnrolmentPeriod(Date startDate, Date endDate) {
        super("message.out.curricular.course.enrolment.period", startDate, endDate);
    }
}