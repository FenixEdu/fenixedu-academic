/*
 * Created on 25/Jul/2003
 *
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */
public class InfoSiteQuestion extends DataTranferObject implements ISiteComponent {
	private InfoQuestion infoQuestion;
	private InfoExecutionCourse executionCourse;

	public InfoSiteQuestion() {
	}

	public InfoQuestion getInfoQuestion() {
		return infoQuestion;
	}

	public void setInfoQuestion(InfoQuestion question) {
		infoQuestion = question;
	}

	public InfoExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	public void setExecutionCourse(InfoExecutionCourse course) {
		executionCourse = course;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSiteQuestion) {
			InfoSiteQuestion infoSiteQuestion = (InfoSiteQuestion) obj;
			result =
				getExecutionCourse().equals(
					infoSiteQuestion.getExecutionCourse())
					&& getInfoQuestion().equals(
						infoSiteQuestion.getInfoQuestion());
		}
		return result;
	}
}