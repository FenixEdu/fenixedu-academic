/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Evaluation extends DomainObject implements IEvaluation {

private String evaluationElements;
private String evaluationElementsEn;
private Integer keyExecutionCourse;
private IDisciplinaExecucao executionCourse;

	/**
	 * 
	 */
	public Evaluation() {}
		
	public Evaluation(IDisciplinaExecucao executionCourse) {
	setExecutionCourse(executionCourse);
	}	

	public Evaluation(IDisciplinaExecucao executionCourse, String evaluationElements) {
		setExecutionCourse(executionCourse);
		setEvaluationElements(evaluationElements);
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
	String result = "[Evaluation";
		result += ", codInt=" + getIdInternal();
		result += ", evaluationElements =" + getEvaluationElements();
		result += ", executionCourse =" + getExecutionCourse();
		result += "]";
	return result;
}

public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IEvaluation) {
			result = getExecutionCourse().equals(((IEvaluation)arg0).getExecutionCourse());
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
