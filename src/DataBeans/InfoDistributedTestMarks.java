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
public class InfoDistributedTestMarks extends InfoObject {
	private List infoStudentTestQuestionList;
	private Double studentTestMark;

	public InfoDistributedTestMarks() {
	}

	public Double getStudentTestMark() {
		return studentTestMark;
	}

	public List getInfoStudentTestQuestionList() {
		return infoStudentTestQuestionList;
	}

	public void setStudentTestMark(Double double1) {
		studentTestMark = double1;
	}

	public void setInfoStudentTestQuestionList(List list) {
		infoStudentTestQuestionList = list;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDistributedTestMarks) {
			InfoDistributedTestMarks infoDistributedTestMarks =
				(InfoDistributedTestMarks) obj;
			result =
				(getStudentTestMark()
					.equals(infoDistributedTestMarks.getStudentTestMark()))
					&& (getInfoStudentTestQuestionList()
						.containsAll(
							infoDistributedTestMarks
								.getInfoStudentTestQuestionList()))
					&& (infoDistributedTestMarks
						.getInfoStudentTestQuestionList()
						.containsAll(
							infoDistributedTestMarks
								.getInfoStudentTestQuestionList()));
		}
		return result;
	}

}
