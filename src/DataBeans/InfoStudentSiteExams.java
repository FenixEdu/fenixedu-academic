/*
 * Created on 27/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 */
public class InfoStudentSiteExams implements ISiteComponent {
	private List examsToEnroll;
	private List examsEnrolled;
	/**
	 * 
	 */
	public InfoStudentSiteExams() {
	}
	public InfoStudentSiteExams(
		List examEnrollmentsToEnroll,
		List examEnrollmentsEnrolled) {
		setExamsEnrolled(examEnrollmentsEnrolled);
		setExamsToEnroll(examEnrollmentsToEnroll);
	}
	/**
	 * @return
	 */
	public List getExamsEnrolled() {
		return examsEnrolled;
	}

	/**
	 * @return
	 */
	public List getExamsToEnroll() {
		return examsToEnroll;
	}

	/**
	 * @param list
	 */
	public void setExamsEnrolled(List list) {
		examsEnrolled = list;
	}

	/**
	 * @param list
	 */
	public void setExamsToEnroll(List list) {
		examsToEnroll = list;
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoStudentSiteExams) {
			InfoStudentSiteExams component = (InfoStudentSiteExams) arg0;
			result =
				listEquals(
					getExamsEnrolled(),
					component.getExamsEnrolled())
					&& listEquals(
						getExamsToEnroll(),
						component.getExamsToEnroll());

		}
		return result;
	}
	/**
	 * @param list
	 * @param list2
	 */
	private boolean listEquals(List list, List list2) {
		boolean result = false;
		if (list == null && list2 == null) {
			result = true;
		} else if (
			list != null
				&& list2 != null
				&& list.containsAll(list2)
				&& list2.containsAll(list)) {
			result = true;
		}

		return result;
	}
}
