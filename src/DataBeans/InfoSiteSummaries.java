/*
 * Created on 21/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 21/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteSummaries implements ISiteComponent {

	private List infoSummaries;
	private InfoExecutionCourse executionCourse;
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
	public InfoSiteSummaries() {
	
	}

	/**
	 * @return
	 */
	public List getInfoSummaries() {
		return infoSummaries;
	}

	/**
	 * @param infoSummaries
	 */
	public void setInfoSummaries(List infoSummaries) {
		this.infoSummaries = infoSummaries;
	}

	public boolean equals(Object obj){
		boolean result= false;
		if (obj instanceof InfoSiteSummaries){
			InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) obj;
			result = getExecutionCourse().equals(infoSiteSummaries.getExecutionCourse())&&
					getInfoSummaries().containsAll(infoSiteSummaries.getInfoSummaries()) &&
					infoSiteSummaries.getInfoSummaries().containsAll(getInfoSummaries());
		}
		
		return result;
	}
}
