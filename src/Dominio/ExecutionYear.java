package Dominio;

import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * ciapl 
 * Dominio
 * 
 */
public class ExecutionYear implements IExecutionYear {

	private PeriodState state;
	protected String year;
	private Integer internalCode;
	/**
	 * Constructor for ExecutionYear.
	 */
	public ExecutionYear() {
	}
	/**
	 * 
	 * @param year
	 */
	public ExecutionYear(String year) {
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
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof IExecutionYear){
			IExecutionYear executionYear = (IExecutionYear) obj;
			return getYear().equals(executionYear.getYear());
		}
		return false;
	}

	public String toString() {
		String result = "[EXECUTION_YEAR";
		result += ", internalCode=" + internalCode;
		result += ", year=" + year;
		result += "]";
		return result;
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionYear#setState(Util.PeriodState)
	 */
	public void setState(PeriodState state) {
		this.state = state;		
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionYear#getState()
	 */
	public PeriodState getState() {
		return this.state;
	}


}
