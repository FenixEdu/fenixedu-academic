package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class LEGIBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String[] DEGREE = { "B5T", "B31"};
    
    private static final String[] PRODUCAO = { "$89"};
    
    private static final String[] EMPREENDIMENTOS = { "$91", "A4O", "1A", "A4H"};
    
    private static final String[] COMMONS_PRODUCAO = { "A4G", "AA9", "A4B", "AFY", "$83"}; 
    
    private static final String[] COMMONS_EMPREENDIMENTOS = { "A4G", "AFY", "$83"};
    
    private static final String PRODUCAO_CODE = "1120";

    private static final String EMPREENDIMENTOS_CODE = "1130";

    public LEGIBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch().getCode().equals(PRODUCAO_CODE)) {
	    return applyBranch(curricularCoursesToBeEnrolledIn, COMMONS_PRODUCAO, DEGREE, PRODUCAO);
	}
	if(studentCurricularPlan.getBranch().getCode().equals(EMPREENDIMENTOS_CODE)) {
	    return applyBranch(curricularCoursesToBeEnrolledIn, COMMONS_EMPREENDIMENTOS, DEGREE, EMPREENDIMENTOS);
	}
	
	return curricularCoursesToBeEnrolledIn;
    }
    
    public List<CurricularCourse2Enroll> applyBranch(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, String[] commons, String[] degree, String[] masterDegree)
	    throws EnrolmentRuleDomainException {

	if(countEnrolments(degree) >= 1) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(masterDegree));
	}
	
	if(countEnrolments(masterDegree) >= 1) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(degree));
	}
	
	return curricularCoursesToBeEnrolledIn;
    }


}
