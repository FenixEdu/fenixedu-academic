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
public class EvaluationMethod extends DomainObject implements IEvaluationMethod {

	private String evaluationElements;
	private String evaluationElementsEn;
	private Integer keyExecutionCourse;
	private IDisciplinaExecucao executionCourse;

	/**
	 * 
	 */
	public EvaluationMethod() {
	}

	public EvaluationMethod(IDisciplinaExecucao executionCourse) {
		setExecutionCourse(executionCourse);
	}

	public EvaluationMethod(IDisciplinaExecucao executionCourse, String evaluationElements) {
		setExecutionCourse(executionCourse);
		setEvaluationElements(evaluationElements);
	}
	public EvaluationMethod(IDisciplinaExecucao executionCourse, String evaluationElements, String evaluationElementsEn) {
		setExecutionCourse(executionCourse);
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

	/**
	 * @return
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @return
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * @param execucao
	 */
	public void setExecutionCourse(IDisciplinaExecucao execucao) {
		executionCourse = execucao;
	}

	/**
	 * @param integer
	 */
	public void setKeyExecutionCourse(Integer integer) {
		keyExecutionCourse = integer;
	}

	public String toString() {
		String result = "[EvaluationMethod";
		result += ", codInt=" + getIdInternal();
		result += ", evaluationElements =" + getEvaluationElements();
		result += ", executionCourse =" + getExecutionCourse();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IEvaluationMethod) {
			result = getExecutionCourse().equals(((IEvaluationMethod) arg0).getExecutionCourse());
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
