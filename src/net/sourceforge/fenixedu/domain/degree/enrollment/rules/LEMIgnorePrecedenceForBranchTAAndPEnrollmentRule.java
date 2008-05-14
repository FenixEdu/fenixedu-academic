/*
 * Created on 15/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LEMIgnorePrecedenceForBranchTAAndPEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;

    private static final String MOTORES_TERMICOS_CODE = "F2";
    private static final String TERMODINAMICA_APLICADA_BRANCH_CODE = "310";
    private static final String PRODUCAO_BRANCH_CODE = "320";

    /**
     *  
     */
    public LEMIgnorePrecedenceForBranchTAAndPEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.degree.enrollment.rules.IEnrollmentRule#apply(java.util.List)
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {

        if (studentCurricularPlan.getBranch() != null
                && (studentCurricularPlan.getBranch().getCode()
                        .equals(TERMODINAMICA_APLICADA_BRANCH_CODE) || studentCurricularPlan.getBranch().getCode().equals(PRODUCAO_BRANCH_CODE))) {
            List curricularCourses = studentCurricularPlan.getDegreeCurricularPlan()
                    .getCurricularCoursesFromArea(studentCurricularPlan.getBranch(),
                            AreaType.SPECIALIZATION);
            CurricularCourse curricularCourse = (CurricularCourse) CollectionUtils.find(
                    curricularCourses, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            CurricularCourse curricularCourseToFind = (CurricularCourse) arg0;
                            return curricularCourseToFind.getCode().equals(MOTORES_TERMICOS_CODE);
                        }
                    });

            CurricularCourse2Enroll curricularCourse2Enroll = transformToCurricularCourse2Enroll(
                    curricularCourse, executionSemester);

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
            CurricularCourse curricularCourse, ExecutionSemester currentExecutionPeriod) {
        return new CurricularCourse2Enroll(curricularCourse, studentCurricularPlan
                .getCurricularCourseEnrollmentType(curricularCourse, currentExecutionPeriod),
                new Boolean(true));
    }

}
