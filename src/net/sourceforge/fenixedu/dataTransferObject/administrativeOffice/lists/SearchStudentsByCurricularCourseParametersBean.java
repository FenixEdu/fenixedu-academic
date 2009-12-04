package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByCurricularCourseParametersBean implements Serializable {

    private ExecutionYear executionYear;

    private DegreeCurricularPlan degreeCurricularPlan;

    private CurricularCourse curricularCourse;

    private Set<Degree> administratedDegrees;

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public CurricularCourse getCurricularCourse() {
	return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = curricularCourse;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public Set<Degree> getAdministratedDegrees() {
	return administratedDegrees;
    }

    public void setAdministratedDegrees(Set<Degree> administratedDegrees) {
	this.administratedDegrees = administratedDegrees;
    }

}
