package Util;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class UniversityCode {

	public static final String IST = "IST";
	public static final String UBI = "UBI";

	private String code;

	public UniversityCode() {
	}

	public UniversityCode(String code) {
		this.code = code;
	}


	/** Getter for property code.
	 * @return Value of property code.
	 *
	 */
	public String getCode() {
		return code;
	}

	public boolean equals(Object o) {
		if (o instanceof UniversityCode) {
			UniversityCode aux = (UniversityCode) o;
			return this.code.equals(aux.getCode());
		} else {
			return false;
		}
	}
}
