package Dominio;

import java.util.List;

import Util.BranchType;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends DomainObject implements IBranch
{
	private Integer keyDegreeCurricularPlan;

	private String name;
	private String code;
	private List scopes;
	private IDegreeCurricularPlan degreeCurricularPlan;

	/**
	 * @author Nuno Correia & Ricardo Rodrigues
	 */
	private String acronym;
	private Integer specializationCredits;
	private Integer secondaryCredits;
	private BranchType branchType;

	public Branch()
	{
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IBranch) {
			IBranch branch = (IBranch) obj;
			result =
				this.getCode().equals(branch.getCode()) &&
//				this.getBranchType().equals(branch.getBranchType()) &&
				this.getDegreeCurricularPlan().equals(branch.getDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "; ";
		result += "acronym = " + this.acronym + "]\n";
		return result;
	}

	/**
	 * @author Fernanda Quitério
	 */
	public Boolean representsCommonBranch() {
//		if (this.name != null && this.name.equals("") && this.code != null && this.code.equals("")) {
//			return Boolean.TRUE;
//		}
//		return Boolean.FALSE;
		if (this.getBranchType().equals(BranchType.COMMON_BRANCH))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
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
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return List
	 */
	public List getScopes() {
		return scopes;
	}

	/**
	 * @param scopes
	 */
	public void setScopes(List scopes) {
		this.scopes = scopes;
	}

	/**
	 * @return
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Integer getKeyDegreeCurricularPlan() {
		return keyDegreeCurricularPlan;
	}

	/**
	 * @param plan
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
		degreeCurricularPlan = plan;
	}

	/**
	 * @param integer
	 */
	public void setKeyDegreeCurricularPlan(Integer integer) {
		keyDegreeCurricularPlan = integer;
	}

	/**
	 * 
	 * @param string
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getAcronym() {
		return acronym;
	}
	
	/**
	 * @return
	 */
	public Integer getSecondaryCredits()
	{
		return secondaryCredits;
	}

	/**
	 * @param secondaryCredits
	 */
	public void setSecondaryCredits(Integer secondaryCredits)
	{
		this.secondaryCredits = secondaryCredits;
	}

	/**
	 * @return
	 */
	public Integer getSpecializationCredits()
	{
		return specializationCredits;
	}

	/**
	 * @param specializationCredits
	 */
	public void setSpecializationCredits(Integer specializationCredits)
	{
		this.specializationCredits = specializationCredits;
	}

	/**
	 * @return Returns the branchType.
	 */
	public BranchType getBranchType()
	{
		return branchType;
	}

	/**
	 * @param branchType The branchType to set.
	 */
	public void setBranchType(BranchType branchType)
	{
		this.branchType = branchType;
	}
}