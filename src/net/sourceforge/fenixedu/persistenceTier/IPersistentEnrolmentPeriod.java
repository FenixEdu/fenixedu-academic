/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;

/**
 * @author jpvl
 */
public interface IPersistentEnrolmentPeriod extends IPersistentObject {

    EnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
			Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    /**
     * 
     * @param plan
     * @return EnrolmentPeriodInCurricularCourses next enrolment period if any.
     *         If doesn't exist other curricular plan then it returns null.
     */
    EnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    public EnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
			Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia;
}