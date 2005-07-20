package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneCurricularCourse extends RestrictionDoneCurricularCourse_Base {

	public RestrictionDoneCurricularCourse() {
        super();
    }
	
	public RestrictionDoneCurricularCourse(Integer number, IPrecedence precedence, ICurricularCourse precedentCurricularCourse) {
		super();
		
        setPrecedence(precedence);
        setPrecedentCurricularCourse(precedentCurricularCourse);
	}

	
	
    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {

        if (precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(
                this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }

     
        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = precedenceContext
                .getStudentCurricularPlan().getAllStudentEnrolledEnrollmentsInExecutionPeriod(
                        precedenceContext.getExecutionPeriod().getPreviousExecutionPeriod());

        List result = (List) CollectionUtils.collect(
                enrollmentsWithEnrolledStateInPreviousExecutionPeriod, new Transformer() {
                    public Object transform(Object obj) {
                        IEnrolment enrollment = (IEnrolment) obj;
                        return enrollment.getCurricularCourse();
                    }
                });

        if (result.contains(this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.TEMPORARY;
        }

        return CurricularCourseEnrollmentType.NOT_ALLOWED;
    }
}