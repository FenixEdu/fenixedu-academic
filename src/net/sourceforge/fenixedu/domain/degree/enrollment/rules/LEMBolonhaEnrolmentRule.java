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

public class LEMBolonhaEnrolmentRule extends BolonhaEnrolmentRule {
    
    private static final String TERMODINAMICA_CODE = "310";
    
    private static final String PRODUCAO_CODE = "320";
    
    private static final String AUTOMACAO_ROBOTICA_CODE = "330";

    private static final String DISSERTACAO_CODE = "$66";
    
    private static final String PROJECTO_SISTEMASI_CODE = "A9C";
    
    private static final String PRODUCAO_MECANICAI_CODE = "A9X";
    
    private static final String PROJECTO_MECANICOI_CODE = "A9Z";

    private static final String[] GROUP_330 = { "A9I", "AQS" };
    
    private static final String[] GROUP_310 = { "6Q", "A6V", "A6W", "A9N"};
    
    private static final String[] GROUP_320_COMMON = { "6Q", "A8Z" };
    
    private static final String[] GROUP_320 = { "6Q", "A8Z", "A9S", "AQK", "AQN" };
    


    public LEMBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null) {
	    if(studentCurricularPlan.getBranch().getCode().equals(TERMODINAMICA_CODE)) {
		return applyTermodinamica(curricularCoursesToBeEnrolledIn);
	    } if(studentCurricularPlan.getBranch().getCode().equals(PRODUCAO_CODE)) {
		return applyProducao(curricularCoursesToBeEnrolledIn);
	    } if(studentCurricularPlan.getBranch().getCode().equals(AUTOMACAO_ROBOTICA_CODE)) {
		return applyAutomacaoRobotica(curricularCoursesToBeEnrolledIn);
	    }
	}
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyProducao(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {

	if(isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PRODUCAO_MECANICAI_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTO_MECANICOI_CODE);
	    int common = countEnrolments(GROUP_320_COMMON);
	    int specific = countEnrolments(GROUP_320);
	    if(common == 0) {
		if(specific == 1) {
		    List<String> coursesToRemove = new ArrayList<String>(GROUP_320.length);
		    coursesToRemove.addAll(Arrays.asList(GROUP_320));
		    coursesToRemove.removeAll(Arrays.asList(GROUP_320_COMMON));
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, coursesToRemove);
		}
	    } else {
		if(specific == 2) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_320));
		}
	    }
	} else {
	    if(isEnrolledInExecutionPeriod(PRODUCAO_MECANICAI_CODE) || isEnrolledInExecutionPeriod(PROJECTO_MECANICOI_CODE)) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    }
	    
	    int common = countEnrolments(GROUP_320_COMMON);
	    int specific = countEnrolments(GROUP_320);
	    if(common == 0) {
		if(specific == 2) {
		    List<String> coursesToRemove = new ArrayList<String>(GROUP_320.length);
		    coursesToRemove.addAll(Arrays.asList(GROUP_320));
		    coursesToRemove.removeAll(Arrays.asList(GROUP_320_COMMON));
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, coursesToRemove);
		    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
		}
	    } else {
		if(specific == 3) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_320));
		    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
		}
	    }
	}
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyTermodinamica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	
	if(isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_310));
	} else {
	    if((countEnrolments(GROUP_310) >= 1)){
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyAutomacaoRobotica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {

	if(isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTO_SISTEMASI_CODE);
	    if(countEnrolments(GROUP_330) == 1) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(GROUP_330));
	    }
	} else {
	    if((countEnrolments(GROUP_330) == 2) || isEnrolledInExecutionPeriod(PROJECTO_SISTEMASI_CODE)){
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }


}
