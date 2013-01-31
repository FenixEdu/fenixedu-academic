package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ExecutionCourseSearchBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ExecutionSemester semester;
	private DegreeCurricularPlan degreeCurricularPlan;
	private CurricularCourse curricularCourse;

	public ExecutionCourseSearchBean() {

	}

	public ExecutionSemester getSemester() {
		return semester;
	}

	public void setSemester(ExecutionSemester semester) {
		this.semester = semester;
	}

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

}
