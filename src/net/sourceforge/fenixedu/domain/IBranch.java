package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.BranchType;

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

    public List getAreaCurricularCourseGroups();

    public List getOptionalCurricularCourseGroups();

    public void setAreaCurricularCourseGroups(List curricularCourseGroups);

    public void setCode(String code);

    public void setName(String name);

    public void setScopes(List scopes);

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);

    public void setAcronym(String acronym);

    public void setSecondaryCredits(Integer secondaryCredits);

    public void setSpecializationCredits(Integer specializationCredits);

    public void setBranchType(BranchType branchType);

    public void setOptionalCurricularCourseGroups(List optionCurricularCourseGroups);

    //Enrollment purposes
    public List getAreaCurricularCourseGroups(AreaType areaType);
}