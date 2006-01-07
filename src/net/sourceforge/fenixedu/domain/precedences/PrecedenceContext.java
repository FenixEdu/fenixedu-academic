package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedenceContext {
    private List curricularCourses2Enroll;

    private ExecutionPeriod executionPeriod;

    private StudentCurricularPlan studentCurricularPlan;

    public PrecedenceContext() {
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public List getCurricularCourses2Enroll() {
        return curricularCourses2Enroll;
    }

    public void setCurricularCourses2Enroll(List curricularCourses2Enroll) {
        this.curricularCourses2Enroll = curricularCourses2Enroll;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }
}