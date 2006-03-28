package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public interface IEnrollmentRule {
    
    public List<CurricularCourse2Enroll> apply(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException;
    
}
