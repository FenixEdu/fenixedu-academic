/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCourses;

/**
 * @author jpvl
 */
public interface IPersistentEnrolmentPeriod extends IPersistentObject {

    IEnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
			Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    /**
     * 
     * @param plan
     * @return IEnrolmentPeriodInCurricularCourses next enrolment period if any.
     *         If doesn't exist other curricular plan then it returns null.
     */
    IEnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    public IEnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
			Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia;
}