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
    private DomainReference<ExecutionSemester> executionSemester;
    private CurricularCourseMarksheetManagementBean curricularCourseBean;
    transient private DomainReference<Teacher> teacher;
    private String url;

    public CurricularCourseMarksheetManagementBean getCurricularCourseBean() {
	return curricularCourseBean;
    }

    public void setCurricularCourseBean(CurricularCourseMarksheetManagementBean curricularCourseBean) {
	this.curricularCourseBean = curricularCourseBean;
    }

    public CurricularCourse getCurricularCourse() {
	return getCurricularCourseBean().getCurricularCourse();
    }

    public Degree getDegree() {
	return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan) : null;
    }

    public boolean hasDegreeCurricularPlan() {
	return getDegreeCurricularPlan() != null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester == null) ? null : this.executionSemester.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public boolean hasExecutionPeriod() {
	return getExecutionPeriod() != null;
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

    public String getDegreeName() {
	return getDegree().getNameFor(getExecutionPeriod()).getContent();
    }

    public String getDegreeCurricularPlanName() {
	return getDegreeCurricularPlan().getName();
    }

    public String getCurricularCourseName() {
	return getCurricularCourse().getName(getExecutionPeriod());
    }

    public String getCurricularCourseNameAndCode() {
	return getCurricularCourse().getName(getExecutionPeriod()) + " " + getCurricularCourse().getAcronym(getExecutionPeriod());
    }
}
