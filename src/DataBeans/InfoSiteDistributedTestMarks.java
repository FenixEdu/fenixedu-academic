/*
 * Created on Oct 24, 2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 *
 */
public class InfoSiteDistributedTestMarks extends DataTranferObject implements ISiteComponent {
	private List infoDistributedTestMarks;
	private List correctAnswersPercentage;
	private List wrongAnswersPercentage;
	private List notAnsweredPercentage;
	private InfoExecutionCourse executionCourse;

	public InfoSiteDistributedTestMarks() {
	}

	public List getCorrectAnswersPercentage() {
		return correctAnswersPercentage;
	}

	public List getInfoDistributedTestMarks() {
		return infoDistributedTestMarks;
	}

	public List getNotAnsweredPercentage() {
		return notAnsweredPercentage;
	}

	public List getWrongAnswersPercentage() {
		return wrongAnswersPercentage;
	}

	public InfoExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	public void setCorrectAnswersPercentage(List list) {
		correctAnswersPercentage = list;
	}

	public void setInfoDistributedTestMarks(List list) {
		infoDistributedTestMarks = list;
	}

	public void setNotAnsweredPercentage(List list) {
		notAnsweredPercentage = list;
	}

	public void setWrongAnswersPercentage(List list) {
		wrongAnswersPercentage = list;
	}

	public void setExecutionCourse(InfoExecutionCourse course) {
		executionCourse = course;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSiteDistributedTestMarks) {
			InfoSiteDistributedTestMarks infoSiteDistributedTestMarks =
				(InfoSiteDistributedTestMarks) obj;
			result =
				getExecutionCourse().equals(
					infoSiteDistributedTestMarks.getExecutionCourse())
					&& getInfoDistributedTestMarks().containsAll(
						infoSiteDistributedTestMarks
							.getInfoDistributedTestMarks())
					&& infoSiteDistributedTestMarks
						.getInfoDistributedTestMarks()
						.containsAll(
						getInfoDistributedTestMarks())
					&& getCorrectAnswersPercentage().containsAll(
						infoSiteDistributedTestMarks
							.getCorrectAnswersPercentage())
					&& infoSiteDistributedTestMarks
						.getCorrectAnswersPercentage()
						.containsAll(
						getCorrectAnswersPercentage())
					&& getWrongAnswersPercentage().containsAll(
						infoSiteDistributedTestMarks
							.getWrongAnswersPercentage())
					&& infoSiteDistributedTestMarks
						.getWrongAnswersPercentage()
						.containsAll(
						getWrongAnswersPercentage())
					&& getNotAnsweredPercentage().containsAll(
						infoSiteDistributedTestMarks
							.getNotAnsweredPercentage())
					&& infoSiteDistributedTestMarks
						.getNotAnsweredPercentage()
						.containsAll(
						getNotAnsweredPercentage());
		}
		return result;
	}
}