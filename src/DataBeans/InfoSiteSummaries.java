/*
 * Created on 21/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

import Util.TipoAula;

/**
 * @author João Mota
 *
 * 21/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteSummaries extends DataTranferObject implements ISiteComponent {

	private List infoSummaries;
	private InfoExecutionCourse executionCourse;
	private TipoAula summaryType;
	private InfoSite infoSite;
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
	/**
	 * @return
	 */
	public TipoAula getSummaryType() {
		return summaryType;
	}

	/**
	 * @param summaryType
	 */
	public void setSummaryType(TipoAula summaryType) {
		this.summaryType = summaryType;
	}
	
	

	/**
	 * @return
	 */
	public InfoSite getInfoSite() {
		return infoSite;
	}

	/**
	 * @param infoSite
	 */
	public void setInfoSite(InfoSite infoSite) {
		this.infoSite = infoSite;
	}

}
