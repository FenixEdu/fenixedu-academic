package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Student;

public class SpecialSeasonStudentEnrollmentBean implements Serializable {

    private Student student;
    private StudentCurricularPlan scp;
    private ExecutionSemester executionSemester;

    public SpecialSeasonStudentEnrollmentBean() {
        super();
    }

    public SpecialSeasonStudentEnrollmentBean(Student student) {
        this();
        setStudent(student);
    }

    public SpecialSeasonStudentEnrollmentBean(Student student, StudentCurricularPlan scp) {
        this();
        setStudent(student);
        setScp(scp);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentCurricularPlan getScp() {
        return scp;
    }

    public void setScp(StudentCurricularPlan scp) {
        this.scp = scp;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

}
