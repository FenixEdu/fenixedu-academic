package Dominio;

import java.util.List;

import Util.AreaType;
import Util.BranchType;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IBranch extends IDomainObject {

	public String getCode();
	public String getName();
	public List getScopes();
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	public String getAcronym();
	public Integer getSecondaryCredits();
	public Integer getSpecializationCredits();
	public BranchType getBranchType();
	public List getCurricularCourseGroups();
    
    public void setCurricularCourseGroups(List curricularCourseGroups) ;
	public void setCode(String code);
	public void setName(String name);
	public void setScopes(List scopes);
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);
	public void setAcronym(String acronym);
	public void setSecondaryCredits(Integer secondaryCredits);
	public void setSpecializationCredits(Integer specializationCredits);
	public void setBranchType(BranchType branchType);
    
	//Enrollment purposes
    public List getCurricularCourseGroups(AreaType areaType);
}