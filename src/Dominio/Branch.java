package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class Branch implements IBranch {

	private Integer internalID;
	private String name;
	private String code;
//	private List associatedCurricularCourses;
	private List associatedStudentCurricularPlans;
	private List scopes;

	public Branch() {
		setName(null);
		setCode(null);
		setInternalID(null);
//		setAssociatedCurricularCourses(null);
		setAssociatedStudentCurricularPlans(null);
		setScopes(null);
	}

	public Branch(String name, String code) {
		this();
		setName(name);
		setCode(code);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof Branch) {
			Branch branch = (Branch) obj;
			resultado = this.getName().equals(branch.getName()) && this.getCode().equals(branch.getCode());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.internalID + "; ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "]\n";
//		result += "associatedCurricularCourses = " + this.associatedCurricularCourses + "; ";
//		result += "associatedStudentCurricularPlans = " + this.associatedStudentCurricularPlans + "]";
		return result;
	}

	/**
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

//	/**
//	 * @return List
//	 */
//	public List getAssociatedCurricularCourses() {
//		return associatedCurricularCourses;
//	}

	/**
	 * @return List
	 */
	public List getAssociatedStudentCurricularPlans() {
		return associatedStudentCurricularPlans;
	}

//	/**
//	 * Sets the associatedCurricularCourses.
//	 * @param associatedCurricularCourses The associatedCurricularCourses to set
//	 */
//	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
//		this.associatedCurricularCourses = associatedCurricularCourses;
//	}

	/**
	 * Sets the associatedStudentCurricularPlans.
	 * @param associatedStudentCurricularPlans The associatedStudentCurricularPlans to set
	 */
	public void setAssociatedStudentCurricularPlans(List associatedStudentCurricularPlans) {
		this.associatedStudentCurricularPlans = associatedStudentCurricularPlans;
	}

	/**
	 * @return List
	 */
	public List getScopes() {
		return scopes;
	}

	/**
	 * Sets the scopes.
	 * @param scopes The scopes to set
	 */
	public void setScopes(List scopes) {
		this.scopes = scopes;
	}

}