/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoEvaluation implements ISiteComponent{

	private InfoExecutionCourse infoExecutionCourse;
	private String evaluationElements;

	public InfoEvaluation() {
	}
	public InfoEvaluation(InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
	}
	public InfoEvaluation(
		InfoExecutionCourse infoExecutionCourse,
		String evaluationElements) {
		setInfoExecutionCourse(infoExecutionCourse);
		setEvaluationElements(evaluationElements);
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

}
