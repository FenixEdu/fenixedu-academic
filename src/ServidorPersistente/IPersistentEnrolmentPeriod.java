/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.EnrolmentPeriodInCurricularCourses;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriodInClasses;
import Dominio.IEnrolmentPeriodInCurricularCourses;

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