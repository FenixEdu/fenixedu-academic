/*
 * Created on Apr 2, 2003
 *
 */
package DataBeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class InfoExamsMap extends InfoObject implements Serializable  {

	List executionCourses;
	List curricularYears;
	Calendar startSeason1;
	Calendar endSeason1;
	Calendar startSeason2;
	Calendar endSeason2;
	InfoExecutionDegree infoExecutionDegree;

	public InfoExamsMap() {
		super();
	}

	public boolean equals(Object obj) {
		if (obj instanceof InfoExamsMap) {
			InfoExamsMap infoExamsMap = (InfoExamsMap) obj;
			return this.getExecutionCourses().size()
				== infoExamsMap.getExecutionCourses().size()
				&& this.getCurricularYears().size()
					== infoExamsMap.getCurricularYears().size();
		}

		return false;
	}

	/**
	 * @return
	 */
	public List getCurricularYears() {
		return curricularYears;
	}

	/**
	 * @return
	 */
	public Calendar getEndSeason1() {
		return endSeason1;
	}

	/**
	 * @return
	 */
	public Calendar getEndSeason2() {
		return endSeason2;
	}

	/**
	 * @return
	 */
	public List getExecutionCourses() {
		return executionCourses;
	}

	/**
	 * @return
	 */
	public Calendar getStartSeason1() {
		return startSeason1;
	}

	/**
	 * @return
	 */
	public Calendar getStartSeason2() {
		return startSeason2;
	}

	/**
	 * @param list
	 */
	public void setCurricularYears(List list) {
		curricularYears = list;
	}

	/**
	 * @param calendar
	 */
	public void setEndSeason1(Calendar calendar) {
		endSeason1 = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEndSeason2(Calendar calendar) {
		endSeason2 = calendar;
	}

	/**
	 * @param list
	 */
	public void setExecutionCourses(List list) {
		executionCourses = list;
	}

	/**
	 * @param calendar
	 */
	public void setStartSeason1(Calendar calendar) {
		startSeason1 = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setStartSeason2(Calendar calendar) {
		startSeason2 = calendar;
	}

	/**
	 * @return Returns the infoExecutionDegree.
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @param infoExecutionDegree The infoExecutionDegree to set.
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
		this.infoExecutionDegree = infoExecutionDegree;
	}

}
