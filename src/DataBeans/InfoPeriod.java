/*
 * Created on Nov 3, 2003
 *
 */
package DataBeans;

import java.util.Calendar;

/**
 * @author Ana e Ricardo
 *
 */
public class InfoPeriod {

	protected Calendar startDate;
	protected Calendar endDate;
	
	/**
	 * @return Returns the endDate.
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

}
