/*
 * Created on 23/Abr/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 *
 * 
 */
public class InfoEvaluation implements ISiteComponent{

	private InfoExecutionCourse infoExecutionCourse;
	private String evaluationElements;
	private String evaluationElementsEn;

	public InfoEvaluation() {
	}
	public InfoEvaluation(InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
	}
	public InfoEvaluation(
		InfoExecutionCourse infoExecutionCourse,
		String evaluationElements,
		String evaluationElementsEn) {
		setInfoExecutionCourse(infoExecutionCourse);
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
	 * @return
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @param string
	 */
	public void setEvaluationElements(String string) {
		evaluationElements = string;
	}

	/**
	 * @param course
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	public String toString() {
		String result = "[Evaluation";

		result += ", evaluationElements =" + getEvaluationElements();
		result += ", evaluationElementsEn =" + getEvaluationElementsEn();
		result += ", executionCourse =" + getInfoExecutionCourse();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoEvaluation) {
			result =
				getInfoExecutionCourse().equals(
					((InfoEvaluation) arg0).getInfoExecutionCourse());
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
