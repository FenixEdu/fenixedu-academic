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
    

    private static final String DISSERTACAO_CODE = "B80";
    
    private static final String[] AUTOMACAO_DEGREE_1_SEM = { "A9C" };
    
    private static final String[] AUTOMACAO_DEGREE_2_SEM = { "F2" , "A9D" };
    
    private static final String[] PRODUCAO_DEGREE_1_SEM = { "A9X", "A9Z" };
    
    private static final String[] PRODUCAO_DEGREE_2_SEM = { "A6U", "AQO", "XG", "F2", "A90", "A9Y", "AA0" };
    
    private static final String[] TERMO_DEGREE_1_SEM = { "A9N", "A6W", "6Q", "A6V" };
    
    private static final String[] TERMO_DEGREE_2_SEM = { "A9P" };
    

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
	}

	return null;
    }

    private List<CurricularCourse2Enroll> applyHidraulica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	// TODO Auto-generated method stub
	return null;
    }

    private List<CurricularCourse2Enroll> applyConstrucaoConstrucao(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	// TODO Auto-generated method stub
	return null;
    }

    private List<CurricularCourse2Enroll> applyConstrucaoEstruturas(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	// TODO Auto-generated method stub
	return null;
    }

    private List<CurricularCourse2Enroll> applyProducao(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(PRODUCAO_DEGREE_2_SEM));
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(PRODUCAO_DEGREE_1_SEM) > 0 
		|| countEnroledOrAprovedEnrolments(PRODUCAO_DEGREE_2_SEM) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }

    /*private List<CurricularCourse2Enroll> applyProducao(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {

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
    }*/

    private List<CurricularCourse2Enroll> applyTermodinamica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(TERMO_DEGREE_2_SEM));
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	} 
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(TERMO_DEGREE_1_SEM) > 0 
		|| countEnroledOrAprovedEnrolments(TERMO_DEGREE_2_SEM) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    //TODO: ver a questão do grupo de opcao do 4º ano
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyAutomacaoRobotica(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(AUTOMACAO_DEGREE_2_SEM));
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(AUTOMACAO_DEGREE_1_SEM) > 0 
		|| countEnroledOrAprovedEnrolments(AUTOMACAO_DEGREE_2_SEM) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    //TODO: ver grupo de opções G
	}
	
	return curricularCoursesToBeEnrolledIn;
    }


}
