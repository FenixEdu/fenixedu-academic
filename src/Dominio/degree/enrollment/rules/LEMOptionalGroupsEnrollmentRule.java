/*
 * Created on 10/Fev/2005
 *
 */
package Dominio.degree.enrollment.rules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import Dominio.exceptions.EnrolmentRuleDomainException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LEMOptionalGroupsEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;

    private IExecutionPeriod executionPeriod;

    public LEMOptionalGroupsEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.degree.enrollment.rules.IEnrollmentRule#apply(java.util.List)
     */
    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {

        ISuportePersistente suportePersistente;
        IPersistentCurricularCourseGroup persistentCurricularCourseGroup;

        try {

            suportePersistente = SuportePersistenteOJB.getInstance();
            persistentCurricularCourseGroup = suportePersistente.getIPersistentCurricularCourseGroup();
            List optionalCurricularCourseGroups = persistentCurricularCourseGroup
                    .readOptionalCurricularCourseGroupsFromArea(studentCurricularPlan.getBranch());

            ICurricularCourseGroup optionalCurricularCourseGroup = (ICurricularCourseGroup) CollectionUtils
                    .find(optionalCurricularCourseGroups, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            ICurricularCourseGroup ccg = (ICurricularCourseGroup) arg0;
                            return ccg.getName().equalsIgnoreCase("Opções 4ºAno 2ºSem");
                        }
                    });

            ICurricularCourse firstOptionalCurricularCourse = (ICurricularCourse) optionalCurricularCourseGroup
                    .getCurricularCourses().get(0);
            ICurricularCourse secondOptionalCurricularCourse = (ICurricularCourse) optionalCurricularCourseGroup
                    .getCurricularCourses().get(1);

            if (studentCurricularPlan.isCurricularCourseEnrolled(firstOptionalCurricularCourse)
                    || studentCurricularPlan.isCurricularCourseApproved(firstOptionalCurricularCourse)) {
                curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
                        secondOptionalCurricularCourse, executionPeriod));
            }

            else if (studentCurricularPlan.isCurricularCourseEnrolled(secondOptionalCurricularCourse)
                    || studentCurricularPlan.isCurricularCourseApproved(secondOptionalCurricularCourse)) {
                curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
                        firstOptionalCurricularCourse, executionPeriod));
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return curricularCoursesToBeEnrolledIn;
    }

    protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(
            ICurricularCourse curricularCourse, IExecutionPeriod currentExecutionPeriod) {
        return new CurricularCourse2Enroll(curricularCourse, studentCurricularPlan
                .getCurricularCourseEnrollmentType(curricularCourse, currentExecutionPeriod),
                new Boolean(true));
    }

}
