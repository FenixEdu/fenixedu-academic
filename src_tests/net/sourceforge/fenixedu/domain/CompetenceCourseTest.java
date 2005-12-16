/*
 * Created on Dec 12, 2005
 *	by jpmsi and lmam
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public class CompetenceCourseTest extends DomainTestBase {

    ICompetenceCourse competenceCourse = null;
    
    IExecutionYear executionYear = null;
    
    ICurricularCourse curricularCourse1 = null;
    ICurricularCourse curricularCourse2 = null;
    ICurricularCourse curricularCourse3 = null;
    
    IDegree degree1 = null;
    IDegree degree2 = null;
        
    protected void setUp() throws Exception {
        super.setUp();
        
        List<IDepartment> departments = new ArrayList<IDepartment>();
        
        competenceCourse = new CompetenceCourse("AS", "Arquitecturas de Software", departments, CurricularStage.APPROVED);
        
        setUpGetActiveEnrollmentEvaluations();
        setUpGetAssociatedCurricularCoursesGroupedByDegree();
    }
    
    private void setUpGetActiveEnrollmentEvaluations() {
        /* Setup execution years */
        executionYear = new ExecutionYear();
        IExecutionYear otherExecutionYear = new ExecutionYear();
        
        /* Setup execution periods */
        IExecutionPeriod executionPeriod1 = new ExecutionPeriod();
        IExecutionPeriod executionPeriod2 = new ExecutionPeriod();
        IExecutionPeriod otherExecutionPeriod = new ExecutionPeriod();
        
        executionPeriod1.setExecutionYear(executionYear);
        executionPeriod2.setExecutionYear(executionYear);
        otherExecutionPeriod.setExecutionYear(otherExecutionYear);

        /* Setup enrollments */
        IEnrolment annuledEnrollment = new Enrolment();
        annuledEnrollment.setEnrollmentState(EnrollmentState.ANNULED);

        IEnrolment numericApprovedEnrollment = new Enrolment();
        numericApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);
        
        IEnrolment apApprovedEnrollment = new Enrolment();
        apApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);

        IEnrolment reprovedEnrollment = new Enrolment();
        reprovedEnrollment.setEnrollmentState(EnrollmentState.NOT_APROVED);
        
        IEnrolment notEvaluatedEnrollment = new Enrolment();
        notEvaluatedEnrollment.setEnrollmentState(EnrollmentState.NOT_EVALUATED);

        IEnrolment otherExecutionPeriodEnrollment = new Enrolment();
        otherExecutionPeriodEnrollment.setEnrollmentState(EnrollmentState.APROVED);
        
        /* Setup enrollment evaluations */
        IEnrolmentEvaluation numericFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        numericFirstEnrolmentEvaluation.setGrade("10");
        
        IEnrolmentEvaluation numericSecondEnrolmentEvaluation = new EnrolmentEvaluation();
        numericSecondEnrolmentEvaluation.setGrade("11");
        
        numericFirstEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        numericSecondEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        
        IEnrolmentEvaluation apFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        apFirstEnrolmentEvaluation.setGrade("AP");
        
        apFirstEnrolmentEvaluation.setEnrolment(apApprovedEnrollment);
        
        IEnrolmentEvaluation reprovedEnrollmentEvaluation = new EnrolmentEvaluation();
        reprovedEnrollmentEvaluation.setGrade("RE");
        
        reprovedEnrollmentEvaluation.setEnrolment(reprovedEnrollment);
        
        IEnrolmentEvaluation notEvaluatedEnrollmentEvaluation = new EnrolmentEvaluation();
        notEvaluatedEnrollmentEvaluation.setGrade("NA");
        
        notEvaluatedEnrollmentEvaluation.setEnrolment(notEvaluatedEnrollment);
        
        IEnrolmentEvaluation otherExecutionPeriodEnrollmentEvaluation = new EnrolmentEvaluation();
        otherExecutionPeriodEnrollmentEvaluation.setGrade("12");
        
        otherExecutionPeriodEnrollmentEvaluation.setEnrolment(otherExecutionPeriodEnrollment);
        
        /* Setup enrollments execution periods */
        annuledEnrollment.setExecutionPeriod(executionPeriod1);
        numericApprovedEnrollment.setExecutionPeriod(executionPeriod1);
        apApprovedEnrollment.setExecutionPeriod(executionPeriod2);
        reprovedEnrollment.setExecutionPeriod(executionPeriod2);
        notEvaluatedEnrollment.setExecutionPeriod(executionPeriod1);
        
        otherExecutionPeriodEnrollment.setExecutionPeriod(otherExecutionPeriod);
        
        /* Setup curricular courses */
        curricularCourse1 = new CurricularCourse();
        curricularCourse2 = new CurricularCourse();
        
        annuledEnrollment.setCurricularCourse(curricularCourse1);
        numericApprovedEnrollment.setCurricularCourse(curricularCourse2);
        apApprovedEnrollment.setCurricularCourse(curricularCourse1);
        reprovedEnrollment.setCurricularCourse(curricularCourse2);
        notEvaluatedEnrollment.setCurricularCourse(curricularCourse2);
        otherExecutionPeriodEnrollment.setCurricularCourse(curricularCourse1);
        
        curricularCourse1.setCompetenceCourse(competenceCourse);
        curricularCourse2.setCompetenceCourse(competenceCourse);
    }

    private void setUpGetAssociatedCurricularCoursesGroupedByDegree() {
        degree1 = new Degree();
        degree2 = new Degree();
        
        IDegreeCurricularPlan degreeCurricularPlan1 = new DegreeCurricularPlan();
        IDegreeCurricularPlan degreeCurricularPlan2 = new DegreeCurricularPlan();
        
        degreeCurricularPlan1.setDegree(degree1);
        degreeCurricularPlan2.setDegree(degree2);
        
        curricularCourse3 = new CurricularCourse();
        curricularCourse3.setCompetenceCourse(competenceCourse);
        
        curricularCourse1.setDegreeCurricularPlan(degreeCurricularPlan1);
        curricularCourse2.setDegreeCurricularPlan(degreeCurricularPlan1);
        curricularCourse3.setDegreeCurricularPlan(degreeCurricularPlan2);
    }
    
	protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetActiveEnrollmentEvaluationsByStudent() {
        assertEquals("Active Enrollment Evaluations By Student Count", 5, competenceCourse.getActiveEnrollmentEvaluations(executionYear).size());
    }

    public void testGetAssociatedCurricularCoursesGroupedByDegree() {
        Map<IDegree, List<ICurricularCourse>> results = competenceCourse.getAssociatedCurricularCoursesGroupedByDegree();
        
        assertEquals("Associated Curricular Courses Degree Count", 2, results.keySet().size());
        assertEquals("Associated Curricular Courses for degree 1", 2, results.get(degree1).size());
        assertEquals("Associated Curricular Courses for degree 2", 1, results.get(degree2).size());
    }
}
