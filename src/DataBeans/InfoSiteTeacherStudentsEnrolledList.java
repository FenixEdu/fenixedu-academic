/*
 * Created on 28/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 */
public class InfoSiteTeacherStudentsEnrolledList implements ISiteComponent {
	private List infoStudents;
	/**
	 * 
	 */
	public InfoSiteTeacherStudentsEnrolledList() {

	}
	public InfoSiteTeacherStudentsEnrolledList(List infoStudents) {
		setInfoStudents(infoStudents);
	}

	public int getSize() {
		if (getInfoStudents() == null) {
			return 0;
		} else {
			return getInfoStudents().size();
		}
	}

	/**
	 * @return
	 */
	public List getInfoStudents() {
		return infoStudents;
	}

	/**
	 * @param list
	 */
	public void setInfoStudents(List list) {
		infoStudents = list;
	}

	public boolean equals(Object arg0) {
		boolean result = false;

		if (arg0 instanceof InfoSiteTeacherStudentsEnrolledList) {
			InfoSiteTeacherStudentsEnrolledList component =
				(InfoSiteTeacherStudentsEnrolledList) arg0;
			result =
				component.getInfoStudents().containsAll(this.getInfoStudents())
					&& this.getInfoStudents().containsAll(
						component.getInfoStudents());
		}

		return result;
	}

}
