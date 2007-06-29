package net.sourceforge.fenixedu.domain.util.search;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class StudentSearchBean implements FactoryExecutor, Serializable {

    private Integer studentNumber;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    public Object execute() {
	return search();
    }

    public Student search() {
	return Student.readStudentByNumber(getStudentNumber());
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(final Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan == null ? null : degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan == null ? null : new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

}
