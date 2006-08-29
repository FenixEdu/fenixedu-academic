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

    private static final String PROJECTOI_CODE = "LU";

    private static final String DISSERTACAO_CODE = "$64";

    private static final String[] COMMONS_810 = { "MD", "L1", "72", "B1W", "AVI", "LT", "A3Q", "A7B" };

    private static final String[] COMMONS_820 = { "L1", "72", "B1V", "B1W", "AVI", "LT", "A7B" };

    private static final String[] DEGREE_810 = {"A2I", "L7", "A2J", "BO", "XG" };
    
    private static final String[] DEGREE_820 = {"A2I", "L7", "A2J", "B1X" };
    
    private static final String[] MASTER_DEGREE_810_1_SEM = { "MD", "L1", "B1W"};
    
    private static final String[] MASTER_DEGREE_820_1_SEM = { "L1", "B1V"};

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
	final boolean projectoI = isEnrolledInExecutionPeriodOrAproved(PROJECTOI_CODE);
	final boolean dissertacao = isEnrolledInExecutionPeriodOrAproved(DISSERTACAO_CODE);

	if (projectoI) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	if (dissertacao) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTOI_CODE);
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_820));
	    if(countEnrolments(MASTER_DEGREE_820_1_SEM) == 1) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE_820_1_SEM));
	    }
	}

	if (!projectoI && !dissertacao) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_820));
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(COMMONS_820));
	}

	return curricularCoursesToBeEnrolledIn;
    }

    private List<CurricularCourse2Enroll> applyProjectoConstrucaoNaval(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	final boolean projectoI = isEnrolledInExecutionPeriodOrAproved(PROJECTOI_CODE);
	final boolean dissertacao = isEnrolledInExecutionPeriodOrAproved(DISSERTACAO_CODE);

	if (projectoI) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	if (dissertacao) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTOI_CODE);
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_810));
	    if(countEnrolments(MASTER_DEGREE_810_1_SEM) == 2) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE_810_1_SEM));
	    }
	}

	if (!projectoI && !dissertacao) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_810));
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(COMMONS_810));
	}

	return curricularCoursesToBeEnrolledIn;
    }

    private void removeAll(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	List<String> coursesToRemove = new ArrayList<String>();
	coursesToRemove.add(PROJECTOI_CODE);
	coursesToRemove.add(DISSERTACAO_CODE);
	coursesToRemove.addAll(Arrays.asList(COMMONS_810));
	coursesToRemove.addAll(Arrays.asList(COMMONS_820));
	coursesToRemove.addAll(Arrays.asList(DEGREE_810));
	coursesToRemove.addAll(Arrays.asList(DEGREE_820));
	removeCurricularCourses(curricularCoursesToBeEnrolledIn, coursesToRemove);
    }


}
