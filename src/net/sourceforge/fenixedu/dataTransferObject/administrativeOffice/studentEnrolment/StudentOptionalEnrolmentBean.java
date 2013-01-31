package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class StudentOptionalEnrolmentBean implements Serializable {

	static private final long serialVersionUID = 1L;

	private StudentCurricularPlan studentCurricularPlan;
	private ExecutionSemester executionSemester;
	private CurriculumGroup curriculumGroup;
	private Context context;
	private DegreeType degreeType;
	private Degree degree;
	private DegreeCurricularPlan degreeCurricularPlan;
	private CurricularCourse selectedCurricularCourse;
	private CurricularRuleLevel curricularRuleLevel;

	public StudentOptionalEnrolmentBean() {
	}

	public StudentOptionalEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
			CurriculumGroup curriculumGroup, Context context) {
		setStudentCurricularPlan(studentCurricularPlan);
		setExecutionPeriod(executionSemester);
		setCurriculumGroup(curriculumGroup);
		setContext(context);
	}

	public StudentCurricularPlan getStudentCurricularPlan() {
		return this.studentCurricularPlan;
	}

	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	public ExecutionSemester getExecutionPeriod() {
		return this.executionSemester;
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}

	public CurriculumGroup getCurriculumGroup() {
		return this.curriculumGroup;
	}

	public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
		this.curriculumGroup = curriculumGroup;
	}

	public Context getContex() {
		return this.context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public Degree getDegree() {
		return this.degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return this.degreeCurricularPlan;
	}

	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	public CurricularCourse getSelectedCurricularCourse() {
		return selectedCurricularCourse;
	}

	public void setSelectedCurricularCourse(CurricularCourse selectedCurricularCourse) {
		this.selectedCurricularCourse = selectedCurricularCourse;
	}

	public CurricularRuleLevel getCurricularRuleLevel() {
		return curricularRuleLevel;
	}

	public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
		this.curricularRuleLevel = curricularRuleLevel;
	}

}
