package DataBeans;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoBranch {

	private String name;
	private String code;
	private List associatedCurricularCourses;
	private List associatedStudentCurricularPlans;

	public InfoBranch() {
		setName(null);
		setCode(null);
		setAssociatedCurricularCourses(null);
		setAssociatedStudentCurricularPlans(null);
	}

	public InfoBranch(String name, String code) {
		setName(name);
		setCode(code);
		setAssociatedCurricularCourses(null);
		setAssociatedStudentCurricularPlans(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoBranch) {
			InfoBranch branch = (InfoBranch) obj;
			resultado = this.getName().equals(branch.getName()) && this.getCode().equals(branch.getCode());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "]";
		result += "associatedCurricularCourses = " + this.associatedCurricularCourses + "; ";
		result += "associatedStudentCurricularPlans = " + this.associatedStudentCurricularPlans + "]";
		return result;
	}

	/**
	 * @return String
	 */
	public String getCode() {
		return code;
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
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return List
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}

	/**
	 * @return List
	 */
	public List getAssociatedStudentCurricularPlans() {
		return associatedStudentCurricularPlans;
	}

	/**
	 * Sets the associatedCurricularCourses.
	 * @param associatedCurricularCourses The associatedCurricularCourses to set
	 */
	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
		this.associatedCurricularCourses = associatedCurricularCourses;
	}

	/**
	 * Sets the associatedStudentCurricularPlans.
	 * @param associatedStudentCurricularPlans The associatedStudentCurricularPlans to set
	 */
	public void setAssociatedStudentCurricularPlans(List associatedStudentCurricularPlans) {
		this.associatedStudentCurricularPlans = associatedStudentCurricularPlans;
	}

}