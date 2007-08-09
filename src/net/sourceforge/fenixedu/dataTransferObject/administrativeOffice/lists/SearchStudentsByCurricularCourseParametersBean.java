package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByCurricularCourseParametersBean implements Serializable {

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    private DomainReference<CurricularCourse> curricularCourse;

    public SearchStudentsByCurricularCourseParametersBean() {
	super();
    }

    public SearchStudentsByCurricularCourseParametersBean(ExecutionYear executionYear, CurricularCourse curricularCourse) {
	super();
	setExecutionYear(executionYear);
	setCurricularCourse(curricularCourse);
	setDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan) : null;
    }

    public CurricularCourse getCurricularCourse() {
	return (this.curricularCourse == null) ? null : this.curricularCourse.getObject();
    }

    private void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(curricularCourse) : null;
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear == null) ? null : this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

}
