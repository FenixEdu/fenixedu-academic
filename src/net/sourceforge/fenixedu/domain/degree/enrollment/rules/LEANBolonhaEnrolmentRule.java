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

public class LEANBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String DISSERTACAO_CODE = "B85";
    
    private static final String SNC_CODE = "A7B";
    
    private static final String[] COMMONS_810_1_SEM = { "MD", "L1"};
    
    private static final String[] COMMONS_810_2_SEM = { "72", "AVI", "LT", "A3Q", "A7B" };
    
    private static final String[] COMMONS_820_1_SEM = { "B1V", "L1" };
    
    private static final String[] COMMONS_820_2_SEM = { "72", "AVI", "LT", "A7B" };
    
    private static final String[] DEGREE_810_1_SEM = {"LU", "A2I"};
    
    private static final String[] DEGREE_810_2_SEM = { "L7", "A2J", "BO", "XG" };

    private static final String[] DEGREE_820_1_SEM = { "LU", "A2I" };
    
    private static final String[] DEGREE_820_2_SEM = { "L7", "A2J", "B1X" };
    
    private static final String[] GROUP_3 = { "BO", "XG", "A7B" };
    
    private static final String[] GROUP_2 = { "A3Q", "A7B" };

    private static final String[] GROUP_3_WITHOUT_SNC = { "BO", "XG" };
    
    private static final String[] GROUP_2_WITHOUT_SNC = { "A3Q" };
    

    public LEANBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null) {
	    if(studentCurricularPlan.getBranch().getCode().equals("810")) {
		return applyProjectoConstrucaoNaval(curricularCoursesToBeEnrolledIn);
	    } 

	    if(studentCurricularPlan.getBranch().getCode().equals("820")) {
		return applyTransportesMaritimos(curricularCoursesToBeEnrolledIn);
	    }
	}
	removeAll(curricularCoursesToBeEnrolledIn);
	return curricularCoursesToBeEnrolledIn;
    }
    
    private List<CurricularCourse2Enroll> applyTransportesMaritimos(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_820_2_SEM));    
	}
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(DEGREE_820_1_SEM) > 0 || 
		countEnroledOrAprovedEnrolments(DEGREE_820_2_SEM) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyProjectoConstrucaoNaval(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_810_2_SEM));
	    if(countEnroledOrAprovedEnrolments(GROUP_2) > 0) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_2));
	    }
	}
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(DEGREE_810_1_SEM) > 0 || 
		countEnroledOrAprovedEnrolments(DEGREE_810_2_SEM) > 0) {
	    int group3 = countEnroledOrAprovedEnrolments(GROUP_3);
	    int group2 = countEnroledOrAprovedEnrolments(GROUP_2);
	    boolean snc = isEnrolledInExecutionPeriodOrAproved(SNC_CODE);
	    if(!snc) {
		if(group2 == 1) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_2_WITHOUT_SNC));
		}
		if(group3 == 1) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_3_WITHOUT_SNC));
		}
	    } else {
		if((group2 == 2 && group3 == 1) || (group3 == 2 && group2 == 1)) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_2));
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_3));
		}
	    }	    
	}
	return curricularCoursesToBeEnrolledIn;
    }

    private void removeAll(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	List<String> coursesToRemove = new ArrayList<String>();
	coursesToRemove.add(DISSERTACAO_CODE);
	coursesToRemove.addAll(Arrays.asList(COMMONS_810_2_SEM));
	coursesToRemove.addAll(Arrays.asList(COMMONS_820_2_SEM));
	coursesToRemove.addAll(Arrays.asList(DEGREE_810_2_SEM));
	coursesToRemove.addAll(Arrays.asList(DEGREE_820_2_SEM));
	removeCurricularCourses(curricularCoursesToBeEnrolledIn, coursesToRemove);
    }

}