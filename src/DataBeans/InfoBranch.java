package DataBeans;



/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoBranch extends InfoObject {

	private String name;
	private String code;
//	private List associatedCurricularCourses;
//	private List associatedStudentCurricularPlans;
//	private List infoScopes;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

	public InfoBranch() {
		setName(null);
		setCode(null);
		setInfoDegreeCurricularPlan(null);
//		setAssociatedCurricularCourses(null);
//		setAssociatedStudentCurricularPlans(null);
//		setInfoScopes(null);
	}

	public InfoBranch(String name, String code) {
		this();
		setName(name);
		setCode(code);
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
		result += "code = " + this.code + "; ";
		result += "idInternal = " + this.getIdInternal() + "]";
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

//	public List getAssociatedCurricularCourses() {
//		return associatedCurricularCourses;
//	}

//	public List getAssociatedStudentCurricularPlans() {
//		return associatedStudentCurricularPlans;
//	}

//	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
//		this.associatedCurricularCourses = associatedCurricularCourses;
//	}

//	public void setAssociatedStudentCurricularPlans(List associatedStudentCurricularPlans) {
//		this.associatedStudentCurricularPlans = associatedStudentCurricularPlans;
//	}

//	public List getInfoScopes() {
//		return infoScopes;
//	}
//
//	public void setInfoScopes(List infoScopes) {
//		this.infoScopes = infoScopes;
//	}

	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan) {
		infoDegreeCurricularPlan = plan;
	}

}