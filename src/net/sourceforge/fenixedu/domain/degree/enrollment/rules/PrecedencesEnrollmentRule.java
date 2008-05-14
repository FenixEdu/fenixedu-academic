package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.PrecedenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesEnrollmentRule implements IEnrollmentRule {
    protected PrecedenceContext precedenceContext;

    public PrecedencesEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.precedenceContext = new PrecedenceContext();
        this.precedenceContext.setStudentCurricularPlan(studentCurricularPlan);
        this.precedenceContext.setExecutionPeriod(executionSemester);
    }

    public List apply(List curricularCoursesWhereToApply) {
        precedenceContext.setCurricularCourses2Enroll(curricularCoursesWhereToApply);

        List curricularCourses2Enroll = new ArrayList();

        for (int i = 0; i < curricularCoursesWhereToApply.size(); i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesWhereToApply
                    .get(i);

            List precedenceList = null;

            precedenceList = curricularCourse2Enroll.getCurricularCourse().getPrecedences();
            
            if (precedenceList == null || precedenceList.isEmpty()) {
                if (!curricularCourses2Enroll.contains(curricularCourse2Enroll)) {
                    curricularCourses2Enroll.add(curricularCourse2Enroll);
                }
            } else {
                int size = precedenceList.size();
                CurricularCourseEnrollmentType evaluate = ((Precedence) precedenceList.get(0))
                        .evaluate(precedenceContext);

                for (int j = 1; j < size; j++) {
                    Precedence precedence = (Precedence) precedenceList.get(j);
                    evaluate = evaluate.or(precedence.evaluate(precedenceContext));
                }

                if(!evaluate.equals(CurricularCourseEnrollmentType.NO_EVALUATE)) {
	                curricularCourse2Enroll.setEnrollmentType(evaluate.and(curricularCourse2Enroll
	                        .getEnrollmentType()));	                
                }
                curricularCourses2Enroll.add(curricularCourse2Enroll);
            }
        }

        List elementsToRemove = (List) CollectionUtils.select(curricularCourses2Enroll, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCourse2Enroll.getEnrollmentType().equals(
                        CurricularCourseEnrollmentType.NOT_ALLOWED);
            }
        });

        curricularCourses2Enroll.removeAll(elementsToRemove);

        curricularCoursesWhereToApply.clear();
        curricularCoursesWhereToApply.addAll(curricularCourses2Enroll);

        return curricularCoursesWhereToApply;
    }

}