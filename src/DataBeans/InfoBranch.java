package DataBeans;

import java.util.StringTokenizer;

import Util.BranchType;

/**
 * @author dcs-rjao
 * @author Fernanda Quitério
 * 
 * 19/Mar/2003
 */

public class InfoBranch extends InfoObject
{

    private String name;
    private String code;
    private Integer specializationCredits;
    private Integer secondaryCredits;
    //	private List associatedCurricularCourses;
    //	private List associatedStudentCurricularPlans;
    //	private List infoScopes;
    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;
	private BranchType branchType;
    
    private String acronym;

    public InfoBranch()
    {
        setName(null);
        setCode(null);
        setInfoDegreeCurricularPlan(null);
        //		setAssociatedCurricularCourses(null);
        //		setAssociatedStudentCurricularPlans(null);
        //		setInfoScopes(null);
    }

    public InfoBranch(String name, String code)
    {
        this();
        setName(name);
        setCode(code);
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoBranch)
        {
            InfoBranch branch = (InfoBranch) obj;
            resultado =
                this.getInfoDegreeCurricularPlan().equals(branch.getInfoDegreeCurricularPlan())
                    && this.getCode().equals(branch.getCode());
        }
        return resultado;
    }

    public String toString()
    {
        String result = "[" + this.getClass().getName() + ": ";
        result += "name = " + this.name + "; ";
        result += "code = " + this.code + "; ";
        result += "idInternal = " + this.getIdInternal() + "]";
        return result;
    }

    /**
     * @author Fernanda Quitério
     */
    public Boolean representsCommonBranch()
    {
        if (this.name != null && this.name.equals("") && this.code != null && this.code.equals(""))
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @author Fernanda Quitério
     */
    /*
     * returns an empty string if there is no branch or branch initials in case it exists
     */
    public String getPrettyCode()
    {
        if (representsCommonBranch().booleanValue())
        {
            return new String("");
        }
        StringBuffer prettyCode = new StringBuffer();
        String namePart = null;
        StringTokenizer stringTokenizer = new StringTokenizer(this.name, " ");
        while (stringTokenizer.hasMoreTokens())
        {
            namePart = stringTokenizer.nextToken();
            if (!namePart.equalsIgnoreCase("RAMO") && namePart.length() > 2)
            {
                prettyCode = prettyCode.append(namePart.substring(0, 1));
            }
        }
        return prettyCode.toString();
    }
    /**
     * @return String
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *          The code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *          The name to set
     */
    public void setName(String name)
    {
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

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan()
    {
        return infoDegreeCurricularPlan;
    }

    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan)
    {
        infoDegreeCurricularPlan = plan;
    }

	/**
	 * @author Nuno Correia
	 * @author Ricardo Rodrigues
	 * 
	 */
    public String getAcronym()
    {
        return acronym;
    }

	public void setAcronym(String acronym)
	{
		this.acronym = acronym;
	}

	/**
	 * @return Returns the secondaryCredits.
	 */
	public Integer getSecondaryCredits()
	{
		return secondaryCredits;
	}

	/**
	 * @param secondaryCredits The secondaryCredits to set.
	 */
	public void setSecondaryCredits(Integer secondaryCredits)
	{
		this.secondaryCredits = secondaryCredits;
	}

	/**
	 * @return Returns the specializationCredits.
	 */
	public Integer getSpecializationCredits()
	{
		return specializationCredits;
	}

	/**
	 * @param specializationCredits The specializationCredits to set.
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