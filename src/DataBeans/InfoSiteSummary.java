/*
 * Created on 21/Jul/2003
 *
 * 
 */
package DataBeans;


/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 21/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteSummary extends DataTranferObject implements ISiteComponent {

	private InfoSummary infoSummary;
	private InfoExecutionCourse executionCourse;
	/**
	 * @return
	 */
	public InfoSummary getInfoSummary() {
		return infoSummary;
	}

	/**
	 * @param infoSummary
	 */
	public void setInfoSummary(InfoSummary infoSummary) {
		this.infoSummary = infoSummary;
	}

	/**
	 * @return
	 */
	public InfoExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @param infoExecutionCourse
	 */
	public void setExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.executionCourse = infoExecutionCourse;
	}

	/**
	 * 
	 */
	public InfoSiteSummary() {

	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSiteSummary) {
			InfoSiteSummary infoSiteSummary = (InfoSiteSummary) obj;
			result =
				getExecutionCourse().equals(
					infoSiteSummary.getExecutionCourse())
					&& getInfoSummary().equals(infoSiteSummary.getInfoSummary());
		}
		return result;
	}

}
