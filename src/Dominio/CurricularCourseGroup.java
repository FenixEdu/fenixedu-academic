/*
 * Created on 25/Nov/2003
 *
 */
package Dominio;

import java.util.List;

import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */

public class CurricularCourseGroup extends DomainObject implements ICurricularCourseGroup
{

    private Integer minimumCredits;
    private Integer maximumCredits;
    private Integer keyBranch;
    private AreaType areaType;
    private IBranch branch;
    private List curricularCourseScopes;

    public CurricularCourseGroup()
    {
    }
    
    public CurricularCourseGroup(Integer idInternal)
    {
    	this.setIdInternal(idInternal);
    }

    public CurricularCourseGroup(
        IBranch branch,
        Integer minCredits,
        Integer maxCredits,
        AreaType areaType)
    {
        setBranch(branch);
        setMinimumCredits(minCredits);
        setMaximumCredits(maxCredits);
        setAreaType(areaType);
    }

    /**
     * @return
     */
    public Integer getKeyBranch()
    {
        return keyBranch;
    }

    /**
     * @param keyBranch
     */
    public void setKeyBranch(Integer keyBranch)
    {
        this.keyBranch = keyBranch;
    }

    /**
     * @return
     */
    public Integer getMaximumCredits()
    {
        return maximumCredits;
    }

    /**
     * @param maximumCredits
     */
    public void setMaximumCredits(Integer maximumCredits)
    {
        this.maximumCredits = maximumCredits;
    }

    /**
     * @return
     */
    public Integer getMinimumCredits()
    {
        return minimumCredits;
    }

    /**
     * @param minimumCredits
     */
    public void setMinimumCredits(Integer minimumCredits)
    {
        this.minimumCredits = minimumCredits;
    }

    /**
     * @return
     */
    public IBranch getBranch()
    {
        return branch;
    }

    /**
     * @param branch
     */
    public void setBranch(IBranch branch)
    {
        this.branch = branch;
    }

    /**
     * @return
     */
    public List getCurricularCourseScopes()
    {
        return curricularCourseScopes;
    }

    /**
     * @param curricularCourseScopes
     */
    public void setCurricularCourseScopes(List curricularCourseScopes)
    {
        this.curricularCourseScopes = curricularCourseScopes;
    }

    /**
     * @return
     */
    public AreaType getAreaType()
    {
        return areaType;
    }

    /**
     * @param areaType
     */
    public void setAreaType(AreaType areaType)
    {
        this.areaType = areaType;
    }

    public String toString()
    {
        return " [minimumCredits] "
            + minimumCredits
            + " [maximumCredits] "
            + maximumCredits
            + " [keyBranch] "
            + keyBranch
            + " [AreaType] "
            + areaType
            + " [Branch] "
            + branch.toString()
            + " [curricularCourseScopes] "
            + curricularCourseScopes;
    }
}
