package DataBeans;


/**
 * @author Nuno & Joana
 */
public class InfoExecutionYear {
	private String year;
	
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

}
