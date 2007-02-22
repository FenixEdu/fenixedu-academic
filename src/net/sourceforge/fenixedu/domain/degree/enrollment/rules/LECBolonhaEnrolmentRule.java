package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LECBolonhaEnrolmentRule extends BolonhaEnrolmentRule {
    
    private static final String CONSTRUCAO_ESTRUTURA_CODE = "111";
    
    private static final String CONSTRUCAO_CONSTRUCAO_CODE = "112";
    
    private static final String HIDRAULICA_CODE = "120";
    
    private static final String PLANEAMENTO_CODE = "130";
    
    private static final String DISSERTACAO_CE_CODE = "BAR";
    
    private static final String DISSERTACAO_CC_CODE = "BAS";
    
    private static final String DISSERTACAO_H_CODE = "BAT";
    
    private static final String DISSERTACAO_P_CODE = "BAU";
    
    private static final String[] CE_DEGREE_GROUP = {"VZ", "YB", "AF6", "AHA", "AJF"};
    
    private static final String[] CE_MASTER_DEGREE_GROUP = {"VZ", "YB", "AF6", "AHA", "AJF", "AF9", "AFA", "AFB" };
    
    private static final String[] CC_MASTER_DEGREE_GROUP = {"YB", "AF6", "AF9", "AFD" };
    
    private static final String[] H_OPTIONAL_GROUP = {"81", "H5", "AFF", "AFI" };
    
    private static final String[] H_DEGREE_GROUP = {"AHA"};
    
    private static final String[] MASTER_DEGREE_PLANEAMENTO_GROUP = {"AHA", "0S", "0T", "A2", "AJF", "VZ", "AFK", "81", "AFL" };
    
    private static final String[] PLANEAMENTO_GROUP = {"0S", "0T", "A2", "AJF", "VZ", "AFK", "81", "AFL" };
    
    

    public LECBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null) {
	    if(studentCurricularPlan.getBranch().getCode().equals(CONSTRUCAO_ESTRUTURA_CODE)) {
		return applyConstrucaoEstruturas(curricularCoursesToBeEnrolledIn);
	    } if(studentCurricularPlan.getBranch().getCode().equals(CONSTRUCAO_CONSTRUCAO_CODE)) {
		return applyConstrucaoConstrucao(curricularCoursesToBeEnrolledIn);
	    } if(studentCurricularPlan.getBranch().getCode().equals(HIDRAULICA_CODE)) {
		return applyHidraulica(curricularCoursesToBeEnrolledIn);
	    } if(studentCurricularPlan.getBranch().getCode().equals(PLANEAMENTO_CODE)) {
		return applyPlaneamento(curricularCoursesToBeEnrolledIn);
	    }

	}
	return curricularCoursesToBeEnrolledIn;
    }
    

    private List<CurricularCourse2Enroll> applyPlaneamento(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_P_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_P_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_P_CODE);
	    if(countEnroledOrAprovedEnrolments(MASTER_DEGREE_PLANEAMENTO_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE_PLANEAMENTO_GROUP));
	    }
	} else {
	    int planeamentoGroup = countEnroledOrAprovedEnrolments(PLANEAMENTO_GROUP);
	    if(planeamentoGroup >= 2) {
		LECOptionalPairGroupsEnrollmentRule rule = new LECOptionalPairGroupsEnrollmentRule(studentCurricularPlan);
		curricularCoursesToBeEnrolledIn = rule.apply(curricularCoursesToBeEnrolledIn);
		if(planeamentoGroup > 2) {
		    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_P_CODE);
		}
	    }
	} 
	
	return curricularCoursesToBeEnrolledIn;
    }


    private List<CurricularCourse2Enroll> applyHidraulica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_H_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_H_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_H_CODE);
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(H_DEGREE_GROUP));
	    if(countEnroledOrAprovedEnrolments(H_OPTIONAL_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(H_OPTIONAL_GROUP));
	    }
	} else {
	    int optionalGroup = countEnroledOrAprovedEnrolments(H_OPTIONAL_GROUP);
	    if(optionalGroup > 2){
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_H_CODE);
		if(optionalGroup >= 3) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(H_OPTIONAL_GROUP));
		}
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyConstrucaoConstrucao(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CC_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CC_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CC_CODE);
	    if(countEnroledOrAprovedEnrolments(CC_MASTER_DEGREE_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(CC_MASTER_DEGREE_GROUP));
	    }
	} else if(countEnroledOrAprovedEnrolments(CC_MASTER_DEGREE_GROUP) > 2){
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CC_CODE);
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyConstrucaoEstruturas(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CE_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CE_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CE_CODE);
	    if(countEnroledOrAprovedEnrolments(CE_MASTER_DEGREE_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(CE_MASTER_DEGREE_GROUP));
	    }
	} else { 
	    int masterDegreeGroup = countEnroledOrAprovedEnrolments(CE_MASTER_DEGREE_GROUP);
	    if(masterDegreeGroup >= 2){
		if(masterDegreeGroup > 2) {
		    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CE_CODE);
		}
		if(countEnroledOrAprovedEnrolments(CE_DEGREE_GROUP) >= 2) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(CE_DEGREE_GROUP));
		}
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

}
