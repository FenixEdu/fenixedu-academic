package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Tânia Pousão
 *
 * 
 */
public class InfoSiteSubmitMarks implements ISiteComponent {
	private InfoEvaluation infoEvaluation;
	private List marksList;

	//errors		
	private List notEnrolmented = null;
	private List errorsMarkNotPublished = null;
	private List mestrado = null;

	

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteStudents
			&& (((((InfoSiteMarks) objectToCompare).getInfoEvaluation() != null
				&& this.getInfoEvaluation() != null
				&& ((InfoSiteMarks) objectToCompare).getInfoEvaluation().equals(this.getInfoEvaluation()))
				|| ((InfoSiteMarks) objectToCompare).getInfoEvaluation() == null
				&& this.getInfoEvaluation() == null))) {
			result = true;
		}

		if (((InfoSiteMarks) objectToCompare).getMarksList() == null && this.getMarksList() == null && result == true) {
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
	public InfoEvaluation getInfoEvaluation() {
		return infoEvaluation;
	}

	/**
	 * @param exam
	 */
	public void setInfoEvaluation(InfoEvaluation evaluation) {
		infoEvaluation = evaluation;
	}


	/**
	 * @return
	 */
	public List getErrorsMarkNotPublished() {
		return errorsMarkNotPublished;
	}

	/**
	 * @return
	 */
	public List getNotEnrolmented() {
		return notEnrolmented;
	}

	/**
	 * @param list
	 */
	public void setErrorsMarkNotPublished(List list) {
		errorsMarkNotPublished = list;
	}

	/**
	 * @param list
	 */
	public void setNotEnrolmented(List list) {
		notEnrolmented = list;
	}

    /**
     * @return Returns the mestrado.
     */
    public List getMestrado()
    {
        return mestrado;
    }

    /**
     * @param mestrado The mestrado to set.
     */
    public void setMestrado(List mestrado)
    {
        this.mestrado = mestrado;
    }

}
