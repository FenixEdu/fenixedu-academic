package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.Enrolment;
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
	
	public RestrictionDoneCurricularCourse(Integer number, Precedence precedence, CurricularCourse precedentCurricularCourse) {
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
                        Enrolment enrollment = (Enrolment) obj;
                        return enrollment.getCurricularCourse();
                    }
                });

        if (result.contains(this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.TEMPORARY;
        }

        CurricularCourseEnrollmentType courseEnrollmentType = CurricularCourseEnrollmentType.DEFINITIVE;
        for (CurricularCourseEquivalence curricularCourseEquivalence : this.getPrecedentCurricularCourse().getCurricularCourseEquivalencesSet()) {
	    for (CurricularCourse curricularCourse : curricularCourseEquivalence.getOldCurricularCoursesSet()) {
		if(precedenceContext.getStudentCurricularPlan().isEquivalentAproved(curricularCourse)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.DEFINITIVE);
		} else if(result.contains(curricularCourse)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.TEMPORARY);
		} else {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.NOT_ALLOWED);
		}
	    }
	    
	    if(courseEnrollmentType.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
		return CurricularCourseEnrollmentType.TEMPORARY;
	    }
	}
        
        return CurricularCourseEnrollmentType.NOT_ALLOWED;
    }
}