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
public class InfoEvaluationMethod implements ISiteComponent{

	
	private InfoCurricularCourse infoCurricularCourse;
	private String evaluationElements;
	private String evaluationElementsEn;

	/**
	 * @return
	 */
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}

	/**
	 * @param infoCurricularCourse
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}

	public InfoEvaluationMethod() {
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

		result += ", evaluationElements =" + getEvaluationElements();
		result += ", evaluationElementsEn =" + getEvaluationElementsEn();
		result += ", curricularCourse =" + getInfoCurricularCourse();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoEvaluationMethod) {
			result =
				getInfoCurricularCourse().equals(
					((InfoEvaluationMethod) arg0).getInfoCurricularCourse());
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
