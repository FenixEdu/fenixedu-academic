/*
 * Created on 23/Abr/2003
 *
 * 
 */
package Dominio;

/**
 * @author João Mota
 *
 * 
 */
public class EvaluationMethod
	extends DomainObject
	implements IEvaluationMethod {

	private String evaluationElements;
	private String evaluationElementsEn;
	private Integer keyCurricularCourse;
	private ICurricularCourse curricularCourse;

	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * @param curricularCourse
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	/**
	 * @return
	 */
	public Integer getKeyCurricularCourse() {
		return keyCurricularCourse;
	}

	/**
	 * @param keyCurricularCourse
	 */
	public void setKeyCurricularCourse(Integer keyCurricularCourse) {
		this.keyCurricularCourse = keyCurricularCourse;
	}

	/**
	 * 
	 */
	public EvaluationMethod() {
	}

	public EvaluationMethod(Integer idInternal) {
		setIdInternal(idInternal);
	}

	/**
	 * @param curricularCourse2
	 * @param string
	 * @param string2
	 */
	public EvaluationMethod(
		ICurricularCourse curricularCourse2,
		String evaluationElements,
		String evaluationElementsEn) {
		setCurricularCourse(curricularCourse2);
		setEvaluationElements(evaluationElements);
		setEvaluationElementsEn(evaluationElementsEn);
	}

	/**
	 * @return
	 */
	public String getEvaluationElements() {
		return evaluationElements;
	}

	/**
	 * @param string
	 */
	public void setEvaluationElements(String string) {
		evaluationElements = string;
	}

	public String toString() {
		String result = "[EvaluationMethod";
		result += ", codInt=" + getIdInternal();
		result += ", evaluationElements =" + getEvaluationElements();
		result += ", executionCourse =" + getCurricularCourse();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IEvaluationMethod) {
			result =
				getCurricularCourse().equals(
					((IEvaluationMethod) arg0).getCurricularCourse());
		}
		return result;
	}

	/**
	 * @return
	 */
	public String getEvaluationElementsEn() {
		return evaluationElementsEn;
	}

	/**
	 * @param string
	 */
	public void setEvaluationElementsEn(String string) {
		evaluationElementsEn = string;
	}

}
