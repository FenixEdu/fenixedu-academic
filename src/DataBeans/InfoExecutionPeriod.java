package DataBeans;

import Util.PeriodState;


/**
 * @author Nuno & Joana
 */
public class InfoExecutionPeriod {

	private String name;
	private InfoExecutionYear infoExecutionYear;
	private PeriodState state;
	private Integer semester;

	public InfoExecutionPeriod() {

	}

	public InfoExecutionPeriod(String name,InfoExecutionYear infoExecutionYear) {
		setName(name);
		setInfoExecutionYear(infoExecutionYear);
		}
	/**
	 * Returns the infoExecutionYear.
	 * @return InfoExecutionYear
	 */
	public InfoExecutionYear getInfoExecutionYear() {
		return infoExecutionYear;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the infoExecutionYear.
	 * @param infoExecutionYear The infoExecutionYear to set
	 */
	public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
		this.infoExecutionYear = infoExecutionYear;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof InfoExecutionPeriod) {
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) obj;
			return (
				getInfoExecutionYear().equals(
					infoExecutionPeriod.getInfoExecutionYear())
					&& getName().equals(infoExecutionPeriod.getName()));

		}
		return false;
	}
	public String toString() {
			String result = "[INFOEXECUTIONPERIOD";
				result += ", name=" + name;
				result += ", infoExecutionYear=" + infoExecutionYear;
				result += "]\n";
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

	/**
	 * @return
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * @param integer
	 */
	public void setSemester(Integer integer) {
		semester = integer;
	}

}
