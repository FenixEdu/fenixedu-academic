package DataBeans;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Tânia Pousão
 *
 * 
 */
public class InfoSiteEnrolmentEvaluation extends DataTranferObject implements ISiteComponent {
	private List enrolmentEvaluations;
	private InfoTeacher infoTeacher;
	private Date lastEvaluationDate;
	private InfoExecutionPeriod infoExecutionPeriod;
	

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteEnrolmentEvaluation
			&& (((((InfoSiteEnrolmentEvaluation) objectToCompare).getLastEvaluationDate() != null
				&& this.getLastEvaluationDate() != null
				&& ((InfoSiteEnrolmentEvaluation) objectToCompare).getLastEvaluationDate().equals(this.getLastEvaluationDate()))
				|| ((InfoSiteEnrolmentEvaluation) objectToCompare).getLastEvaluationDate() == null
				&& this.getLastEvaluationDate() == null))
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
	public Date getLastEvaluationDate() {
		return lastEvaluationDate;
	}

	/**
	 * @param lastEvaluationDate
	 */
	public void setLastEvaluationDate(Date lastEvaluationDate) {
		this.lastEvaluationDate = lastEvaluationDate;
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

	

	/**
	 * @return Returns the infoExecutionPeriod.
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod()
	{
		return infoExecutionPeriod;
	}

	/**
	 * @param infoExecutionPeriod The infoExecutionPeriod to set.
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
	{
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

}
