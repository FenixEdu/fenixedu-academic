/*
 * Inscricao.java
 *
 * Created on 20 de Outubro de 2002, 17:26
 */

package Dominio;


/**
 *
 * @author  tfc130
 */
public class Enrolment implements IEnrolment {
	private IStudentCurricularPlan studentCurricularPlan;
	private ICurricularCourse curricularCourse;

	// c�digos internos da base de dados
	private Integer internalID;
	private Integer studentCurricularPlanKey;
	private Integer curricularCourseKey;

	/** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
	public Enrolment() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		
		if (obj instanceof IEnrolment) {			
			IEnrolment enrolment = (IEnrolment) obj;
			
			resultado =	(getStudentCurricularPlan().equals(enrolment.getStudentCurricularPlan())) &&
			            (getCurricularCourse().equals(enrolment.getCurricularCourse()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[INSCRICAO\n";
		result += "chavePlanoCurricularAluno=\n" + studentCurricularPlan+"\n";
		result += "chaveDisciplinaCurricular=\n" + curricularCourse.getName();
		result += "]";
		return result;
	}

	/**
	 * Returns the curricularCourse.
	 * @return ICurricularCourse
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * Returns the curricularCourseKey.
	 * @return Integer
	 */
	public Integer getCurricularCourseKey() {
		return curricularCourseKey;
	}

	/**
	 * Returns the internalID.
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

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

	/**
	 * Sets the curricularCourse.
	 * @param curricularCourse The curricularCourse to set
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	/**
	 * Sets the curricularCourseKey.
	 * @param curricularCourseKey The curricularCourseKey to set
	 */
	public void setCurricularCourseKey(Integer curricularCourseKey) {
		this.curricularCourseKey = curricularCourseKey;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

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

}
