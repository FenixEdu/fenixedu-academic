package DataBeans;

import java.io.Serializable;

import Util.PeriodState;


/**
 * @author Nuno & Joana
 */
public class InfoExecutionYear implements Serializable{
	private String year;
	private PeriodState state;
	
	public InfoExecutionYear() {
	}

	public InfoExecutionYear(String year) {
		setYear(year);	
	}
	/**
	 * Returns the year.
	 * @return String
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the year.
	 * @param year The year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof InfoExecutionYear){
			InfoExecutionYear infoExecutionYear = (InfoExecutionYear) obj;
			return getYear().equals(infoExecutionYear.getYear());
		}
		return false;
	}
	
	public String toString() {
			String result = "[INFOEXECUTIONYEAR";
				result += ", year=" + year;
				result += "]";
			return result;
		}
	/**
	 * @return PeriodState
	 */
	public PeriodState getState() {
		return state;
	}

	/**
	 * Sets the periodState.
	 * @param periodState The periodState to set
	 */
	public void setState(PeriodState state) {
		this.state = state;
	}

}
