package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

public class MarkSheetManagementBaseBean implements Serializable {

    private Degree degree;
    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionSemester executionSemester;
    private CurricularCourseMarksheetManagementBean curricularCourseBean;
    transient private Teacher teacher;
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

    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }
}
