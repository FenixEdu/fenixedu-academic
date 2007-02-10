package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEMBolonhaEnrolmentRule extends BolonhaEnrolmentRule {
    
    private static final String TERMODINAMICA_CODE = "310";
    
    private static final String PRODUCAO_CODE = "320";
    
    private static final String AUTOMACAO_ROBOTICA_CODE = "330";

    private static final String DISSERTACAO_CODE = "B80";
    
    private static final String TURBOMAQUINAS_CODE = "56";
    
    private static final String ORGAOS_MAQUINAS_CODE = "56";
    
    private static final String[] AUTOMACAO_DEGREE_1_SEM = { "A9C" };
    
    private static final String[] AUTOMACAO_DEGREE_2_SEM = { "F2" , "A9D" };
    
    private static final String[] PRODUCAO_DEGREE_1_SEM = { "A9X", "A9Z" };
    
    private static final String[] PRODUCAO_DEGREE_2_SEM = { "AQO", "XG", "F2", "A9Y", "AA0" };
    
    private static final String[] TERMO_DEGREE_1_SEM = { "A9N", "A6W", "6Q", "A6V" };
    
    private static final String[] TERMO_DEGREE_2_SEM = { "A9P" };
    
    private static final String[] TERMO_4_YEAR_GROUP = { "56", "WU", "68"};
    
    private static final String[] PRODUCAO_4_YEAR_2_SEM = { "A6U", "A90"};
    

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
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(PRODUCAO_DEGREE_2_SEM));
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    if(countEnroledOrAprovedEnrolments(PRODUCAO_4_YEAR_2_SEM) >= 1) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(PRODUCAO_4_YEAR_2_SEM));
	    }
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
	    if(countEnroledOrAprovedEnrolments(TERMO_4_YEAR_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(TERMO_4_YEAR_GROUP));
	    }
	} else if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(TERMO_DEGREE_1_SEM) > 0 
		|| countEnroledOrAprovedEnrolments(TERMO_DEGREE_2_SEM) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    if(isAproved(TURBOMAQUINAS_CODE)) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, ORGAOS_MAQUINAS_CODE);
	    } else {
		if(countEnroledOrAprovedEnrolments(TERMO_4_YEAR_GROUP) >= 2) {
		    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(TERMO_4_YEAR_GROUP));
		}
	    }
	} else {
	    if(countEnroledOrAprovedEnrolments(TERMO_4_YEAR_GROUP) >= 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(TERMO_4_YEAR_GROUP));
	    }
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
