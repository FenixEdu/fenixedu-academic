package Dominio;

import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Enrolment extends DomainObject implements IEnrolment {

	private IStudentCurricularPlan studentCurricularPlan;
//	private ICurricularCourse curricularCourse;
	private IExecutionPeriod executionPeriod;
	private EnrolmentState enrolmentState;
	private EnrolmentEvaluationType enrolmentEvaluationType;
	private ICurricularCourseScope curricularCourseScope;

//	private Integer internalID;
	private Integer studentCurricularPlanKey;
//	private Integer curricularCourseKey;
	private Integer curricularCourseScopeKey;
	private Integer keyExecutionPeriod;
	
	private List evaluations;

	private String ojbConcreteClass;

	public Enrolment() {
		this.ojbConcreteClass = this.getClass().getName();
	}

	public Enrolment(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope, EnrolmentState state) {
		this();
		setCurricularCourseScope(curricularCourseScope);
		setStudentCurricularPlan(studentCurricularPlan);
		setEnrolmentState(state);
	}

	public Enrolment(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourseScope curricularCourseScope,
		EnrolmentState state,
		IExecutionPeriod executionPeriod) {
		this();
		setCurricularCourseScope(curricularCourseScope);
		setStudentCurricularPlan(studentCurricularPlan);
		setEnrolmentState(state);
		setExecutionPeriod(executionPeriod);
	}

	public Enrolment(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope, 
	EnrolmentState state, IExecutionPeriod executionPeriod, EnrolmentEvaluationType enrolmentEvaluationType) {
		this();
		setCurricularCourseScope(curricularCourseScope);
		setStudentCurricularPlan(studentCurricularPlan);
		setEnrolmentState(state);
		setExecutionPeriod(executionPeriod);
		setEnrolmentEvaluationType(enrolmentEvaluationType);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEnrolment) {
			IEnrolment enrolment = (IEnrolment) obj;
						
			resultado =
				this.getStudentCurricularPlan().equals(enrolment.getStudentCurricularPlan())
					&& this.getCurricularCourseScope().equals(enrolment.getCurricularCourseScope())
					&& this.getExecutionPeriod().equals(enrolment.getExecutionPeriod());
		}
		return resultado;
	}
		
	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "studentCurricularPlan = " + this.studentCurricularPlan + "; ";
		result += "enrolmentState = " + this.enrolmentState + "; ";
		result += "execution Period = " + this.executionPeriod + "; ";
		result += "curricularCourseScope = " + this.curricularCourseScope + "]\n";
//		result += "curricularCourse = " + this.curricularCourse + "]\n";
		return result;
	}

//	public ICurricularCourse getCurricularCourse() {
//		return curricularCourse;
//	}
//
//	public Integer getCurricularCourseKey() {
//		return curricularCourseKey;
//	}

//	public Integer getInternalID() {
//		return internalID;
//	}

	/**
	 * Returns the studentCurricularPlan.
	 * @return IStudentCurricularPlan
	 */
	public IStudentCurricularPlan getStudentCurricularPlan() {
		return studentCurricularPlan;
	}

	/**
	 * Returns the studentCurricularPlanKey.
	 * @return Integer
	 */
	public Integer getStudentCurricularPlanKey() {
		return studentCurricularPlanKey;
	}

//	public void setCurricularCourse(ICurricularCourse curricularCourse) {
//		this.curricularCourse = curricularCourse;
//	}
//
//	public void setCurricularCourseKey(Integer curricularCourseKey) {
//		this.curricularCourseKey = curricularCourseKey;
//	}

//	/**
//	 * Sets the internalID.
//	 * @param internalID The internalID to set
//	 */
//	public void setInternalID(Integer internalID) {
//		this.internalID = internalID;
//	}

	/**
	 * Sets the studentCurricularPlan.
	 * @param studentCurricularPlan The studentCurricularPlan to set
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * Sets the studentCurricularPlanKey.
	 * @param studentCurricularPlanKey The studentCurricularPlanKey to set
	 */
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey) {
		this.studentCurricularPlanKey = studentCurricularPlanKey;
	}

	/**
	 * @return EnrolmentState
	 */
	public EnrolmentState getEnrolmentState() {
		return enrolmentState;
	}

	/**
	 * Sets the enrolmentState.
	 * @param enrolmentState The enrolmentState to set
	 */
	public void setEnrolmentState(EnrolmentState state) {
		this.enrolmentState = state;
	}

	/**
	 * @return
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}

	/**
	 * @return
	 */
	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	/**
	 * @param period
	 */
	public void setExecutionPeriod(IExecutionPeriod period) {
		executionPeriod = period;
	}

	/**
	 * @param integer
	 */
	public void setKeyExecutionPeriod(Integer integer) {
		keyExecutionPeriod = integer;
	}

	/**
	 * @return String
	 */
	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	/**
	 * Sets the ojbConcreteClass.
	 * @param ojbConcreteClass The ojbConcreteClass to set
	 */
	public void setOjbConcreteClass(String ojbConcreteClass) {
		this.ojbConcreteClass = ojbConcreteClass;
	}

//	public ICurricularCourse getRealCurricularCourse() {
//		return this.getCurricularCourse();	
//	}

	public EnrolmentEvaluationType getEnrolmentEvaluationType() {
		return this.enrolmentEvaluationType;
	}

	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
		this.enrolmentEvaluationType = type;
	}

	public List getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List list) {
		evaluations = list;
	}

	public ICurricularCourseScope getCurricularCourseScope() {
		return curricularCourseScope;
	}

	public Integer getCurricularCourseScopeKey() {
		return curricularCourseScopeKey;
	}

	public void setCurricularCourseScope(ICurricularCourseScope scope) {
		curricularCourseScope = scope;
	}

	public void setCurricularCourseScopeKey(Integer integer) {
		curricularCourseScopeKey = integer;
	}

}
