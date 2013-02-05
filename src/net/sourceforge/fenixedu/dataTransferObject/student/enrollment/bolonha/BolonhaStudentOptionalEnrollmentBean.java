package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.student.Registration;

public class BolonhaStudentOptionalEnrollmentBean implements Serializable {

    static private final long serialVersionUID = -4707696936211804716L;

    private DegreeType degreeType;

    private Degree degree;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionSemester executionSemester;

    private CurricularCourse selectedOptionalCurricularCourse;

    private StudentCurricularPlan studentCurricularPlan;

    private IDegreeModuleToEvaluate selectedDegreeModuleToEnrol;

    public BolonhaStudentOptionalEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final IDegreeModuleToEvaluate degreeModuleToEnrol) {
        super();

        setExecutionPeriod(executionSemester);
        setSelectedDegreeModuleToEnrol(degreeModuleToEnrol);
        setStudentCurricularPlan(studentCurricularPlan);

    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public boolean hasDegreeType() {
        return getDegreeType() != null;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public boolean hasDegree() {
        return getDegree() != null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public CurricularCourse getSelectedOptionalCurricularCourse() {
        return this.selectedOptionalCurricularCourse;
    }

    public void setSelectedOptionalCurricularCourse(CurricularCourse selectedOptionalCurricularCourse) {
        this.selectedOptionalCurricularCourse = selectedOptionalCurricularCourse;
    }

    public IDegreeModuleToEvaluate getSelectedDegreeModuleToEnrol() {
        return selectedDegreeModuleToEnrol;
    }

    public void setSelectedDegreeModuleToEnrol(IDegreeModuleToEvaluate selectedDegreeModuleToEnrol) {
        this.selectedDegreeModuleToEnrol = selectedDegreeModuleToEnrol;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

}
