package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Tânia Pousão
 *
 * 
 */
public class InfoSiteMarks implements ISiteComponent {
	private InfoExam infoExam;
	private List marksList;
	

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteStudents
			&& (((((InfoSiteMarks) objectToCompare).getInfoExam() != null
				&& this.getInfoExam() != null
				&& ((InfoSiteMarks) objectToCompare).getInfoExam().equals(this.getInfoExam()))
				|| ((InfoSiteMarks) objectToCompare).getInfoExam() == null
				&& this.getInfoExam() == null))) {
			result = true;
		}

		if (((InfoSiteMarks) objectToCompare).getMarksList() == null && this.getMarksList() == null) {
			return true;
		}

		if (((InfoSiteMarks) objectToCompare).getMarksList() == null
			|| this.getMarksList() == null
			|| ((InfoSiteMarks) objectToCompare).getMarksList().size() != this.getMarksList().size()) {
			return false;
		}

		ListIterator iter1 = ((InfoSiteMarks) objectToCompare).getMarksList().listIterator();
		ListIterator iter2 = this.getMarksList().listIterator();
		while (result && iter1.hasNext()) {
			InfoMark infoMark1 = (InfoMark) iter1.next();
			InfoMark infoMark2 = (InfoMark) iter2.next();
			if (!infoMark1.equals(infoMark2)) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return
	 */
	public List getMarksList() {
		return marksList;
	}

	/**
	 * @param list
	 */
	public void setMarksList(List list) {
		marksList = list;
	}

	/**
	 * @return
	 */
	public InfoExam getInfoExam() {
		return infoExam;
	}

	/**
	 * @param exam
	 */
	public void setInfoExam(InfoExam exam) {
		infoExam = exam;
	}

}
