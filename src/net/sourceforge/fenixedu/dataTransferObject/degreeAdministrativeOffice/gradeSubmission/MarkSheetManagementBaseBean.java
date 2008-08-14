package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;

public class MarkSheetManagementBaseBean implements Serializable {

    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    private DomainReference<CurricularCourse> curricularCourse;
    private DomainReference<ExecutionSemester> executionSemester;
    transient private DomainReference<Teacher> teacher;

    private String url;

    public CurricularCourse getCurricularCourse() {
	return (this.curricularCourse == null) ? null : this.curricularCourse.getObject();
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(curricularCourse) : null;
    }

    public Degree getDegree() {
	return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester == null) ? null : this.executionSemester.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public Teacher getTeacher() {
	return (this.teacher == null) ? null : this.teacher.getObject();
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = (teacher != null) ? new DomainReference<Teacher>(teacher) : null;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

}
