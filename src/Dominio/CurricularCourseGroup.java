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
    private List curricularCourses;
    private List scientificAreas;

    public CurricularCourseGroup()
    {
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
    public List getCurricularCourses()
    {
        return curricularCourses;
    }

    /**
     * @param curricularCourses
     */
    public void setCurricularCourses(List curricularCourses)
    {
        this.curricularCourses = curricularCourses;
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

    /**
	 * @return Returns the scientificAreas.
	 */
	public List getScientificAreas()
	{
		return scientificAreas;
	}

	/**
	 * @param scientificAreas The scientificAreas to set.
	 */
	public void setScientificAreas(List scientificAreas)
	{
		this.scientificAreas = scientificAreas;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if ((result) && (obj instanceof ICurricularCourseGroup))
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) obj;
			result =
				curricularCourseGroup.getBranch().equals(this.getBranch()) &&
				curricularCourseGroup.getIdInternal().equals(this.getIdInternal());
		}
		return result;
	}
	
	public String toString()
	{
		return "minimumCredits[" + minimumCredits + "] maximumCredits[" + maximumCredits + "] branch[" + branch.getName() + "]";
	}
}