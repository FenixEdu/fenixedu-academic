/*
 * Created on 15/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LEMIgnorePrecedenceForBranchTAEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;
    private IExecutionPeriod executionPeriod;

    private static final String MOTORES_TERMICOS_CODE = "F2";
    private static final String TERMODINAMICA_APLICADA_BRANCH_CODE = "310";

    /**
     *  
     */
    public LEMIgnorePrecedenceForBranchTAEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.degree.enrollment.rules.IEnrollmentRule#apply(java.util.List)
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {

        if (studentCurricularPlan.getBranch() != null
                && studentCurricularPlan.getBranch().getCode()
                        .equals(TERMODINAMICA_APLICADA_BRANCH_CODE)) {
            List curricularCourses = studentCurricularPlan.getDegreeCurricularPlan()
                    .getCurricularCoursesFromArea(studentCurricularPlan.getBranch(),
                            AreaType.SPECIALIZATION);
            ICurricularCourse curricularCourse = (ICurricularCourse) CollectionUtils.find(
                    curricularCourses, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            ICurricularCourse curricularCourseToFind = (ICurricularCourse) arg0;
                            return curricularCourseToFind.getCode().equals(MOTORES_TERMICOS_CODE);
                        }
                    });

            CurricularCourse2Enroll curricularCourse2Enroll = transformToCurricularCourse2Enroll(
                    curricularCourse, executionPeriod);

            if (curricularCourse2Enroll.getEnrollmentType().equals(
                    CurricularCourseEnrollmentType.NOT_ALLOWED))
                return curricularCoursesToBeEnrolledIn;

            curricularCourse2Enroll.setAccumulatedWeight(studentCurricularPlan
                    .getCurricularCourseAcumulatedEnrollments(curricularCourse));
            curricularCoursesToBeEnrolledIn.add(curricularCourse2Enroll);
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
