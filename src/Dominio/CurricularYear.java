package Dominio;

/**
 * @author dcs-rjao
 *
 * 21/Mar/2003
 */

public class CurricularYear extends DomainObject implements ICurricularYear {

	//private Integer internalID;
	private Integer year;

	public CurricularYear() {
		setYear(null);
		//setInternalID(null);
	}

	public CurricularYear(Integer year) {
		setYear(year);
		//setInternalID(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ICurricularYear) {
			ICurricularYear curricularYear = (ICurricularYear) obj;
			resultado = (this.getYear().equals(curricularYear.getYear()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		//result += "idInternal = " + this.internalID + "; ";
		result += "year = " + this.year + "]\n";
		return result;
	}

	
//	/**
//	 * @return Integer
//	 */
//	public Integer getInternalID() {
//		return internalID;
//	}

	/**
	 * @return Integer
	 */
	public Integer getYear() {
		return year;
	}

//	/**
//	 * Sets the internalID.
//	 * @param internalID The internalID to set
//	 */
//	public void setInternalID(Integer internalID) {
//		this.internalID = internalID;
//	}

	/**
	 * Sets the year.
	 * @param year The year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

}