package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Tânia Pousão
 *
 * 
 */
public class InfoSiteEnrolmentEvaluation implements ISiteComponent {
	private List enrolmentEvaluations;
	private InfoTeacher infoTeacher;

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteEnrolmentEvaluation
			&& (((((InfoSiteEnrolmentEvaluation) objectToCompare).getInfoTeacher() != null
				&& this.getInfoTeacher() != null
				&& ((InfoSiteEnrolmentEvaluation) objectToCompare).getInfoTeacher().equals(this.getInfoTeacher()))
				|| ((InfoSiteEnrolmentEvaluation) objectToCompare).getInfoTeacher() == null
				&& this.getInfoTeacher() == null))) {
			result = true;
		}

		if (((InfoSiteEnrolmentEvaluation) objectToCompare).getEnrolmentEvaluations() == null
			&& this.getEnrolmentEvaluations() == null
			&& result == true) {
			return true;
		}

		if (((InfoSiteEnrolmentEvaluation) objectToCompare).getEnrolmentEvaluations() == null
			|| this.getEnrolmentEvaluations() == null
			|| ((InfoSiteEnrolmentEvaluation) objectToCompare).getEnrolmentEvaluations().size()
				!= this.getEnrolmentEvaluations().size()) {
			return false;
		}

		ListIterator iter1 = ((InfoSiteEnrolmentEvaluation) objectToCompare).getEnrolmentEvaluations().listIterator();
		ListIterator iter2 = this.getEnrolmentEvaluations().listIterator();
		while (result && iter1.hasNext()) {
			InfoEnrolmentEvaluation infoEnrolmentEvaluation1 = (InfoEnrolmentEvaluation) iter1.next();
			InfoEnrolmentEvaluation infoEnrolmentEvaluation2 = (InfoEnrolmentEvaluation) iter2.next();
			if (!infoEnrolmentEvaluation1.equals(infoEnrolmentEvaluation2)) {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * @return
	 */
	public List getEnrolmentEvaluations() {
		return enrolmentEvaluations;
	}

	/**
	 * @param enrolmentEvaluations
	 */
	public void setEnrolmentEvaluations(List enrolmentEvaluations) {
		this.enrolmentEvaluations = enrolmentEvaluations;
	}

	/**
	 * @return
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param infoTeacher
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher) {
		this.infoTeacher = infoTeacher;
	}

}
