/*
 * Created on 4/Ago/2003
 *
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 *
 */

public class InfoSiteStudentsWithoutGroup extends DataTranferObject implements ISiteComponent {

	private List infoStudentList;
	private Integer groupNumber;
	
	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if (objectToCompare instanceof InfoSiteStudentsWithoutGroup) {

			result = (this.getGroupNumber().equals(((InfoSiteStudentsWithoutGroup) objectToCompare).getGroupNumber()));
			
		}

		if (((InfoSiteStudentsWithoutGroup) objectToCompare).getInfoStudentList() == null
			&& this.getInfoStudentList() == null
			&& result == true) {
				
			return true;
		}
		if (((InfoSiteStudentsWithoutGroup) objectToCompare).getInfoStudentList() == null
			|| this.getInfoStudentList() == null
			|| ((InfoSiteStudentsWithoutGroup) objectToCompare).getInfoStudentList().size() != this.getInfoStudentList().size()) {
				
			return false;
		}

		ListIterator iter1 = ((InfoSiteStudentsWithoutGroup) objectToCompare).getInfoStudentList().listIterator();
		ListIterator iter2 = this.getInfoStudentList().listIterator();
		while (result && iter1.hasNext()) {
		
			InfoStudent infoStudent1 = (InfoStudent) iter1.next();
			InfoStudent infoStudent2 = (InfoStudent) iter2.next();
			if (!infoStudent1.equals(infoStudent2)) {
				
				result = false;
			}
		}
		return result;
	}

	/**
		 * @return
		 */
	public List getInfoStudentList() {
		return infoStudentList;
	}

	/**
	 * @param list
	 */
	public void setInfoStudentList(List infoStudentList) {
		this.infoStudentList = infoStudentList;
	}

	/**
	 * @return
	 */
	public Integer getGroupNumber() {
		return groupNumber;
	}

	/**
	 * @param integer
	 */
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFO_SITE_STUDENTS_WITHOUT";
		result += ", infoStudentList=" + getInfoStudentList();
		result += ", groupNumber=" + getGroupNumber();
		result += "]";
		return result;
	}
}
