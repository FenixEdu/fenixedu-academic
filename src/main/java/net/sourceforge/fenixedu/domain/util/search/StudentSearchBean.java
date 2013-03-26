package net.sourceforge.fenixedu.domain.util.search;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class StudentSearchBean implements FactoryExecutor, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer studentNumber;

    private DegreeCurricularPlan degreeCurricularPlan;

    private DegreeCurricularPlan oldDegreeCurricularPlan;

    @Override
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
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public DegreeCurricularPlan getOldDegreeCurricularPlan() {
        return this.oldDegreeCurricularPlan;
    }

    public void setOldDegreeCurricularPlan(DegreeCurricularPlan oldDegreeCurricularPlan) {
        this.oldDegreeCurricularPlan = oldDegreeCurricularPlan;
    }

}
