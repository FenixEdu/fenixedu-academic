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

    /**
     * @param degreeCurricularPlan
     * @return
     */
    IEnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    /**
     * 
     * @param plan
     * @return IEnrolmentPeriodInCurricularCourses next enrolment period if any.
     *         If doesn't exist other curricular plan then it returns null.
     */
    IEnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan plan) throws ExcepcaoPersistencia;

    public EnrolmentPeriodInCurricularCourses readEnrolmentPeriodByKeyAndDegreeCurricularPlan(
            Integer key, IDegreeCurricularPlan plan) throws ExcepcaoPersistencia;

    public IEnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia;

    public List readAllEnrollmentPeriodsInClasses() throws ExcepcaoPersistencia;
}