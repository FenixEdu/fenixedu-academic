/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.util.Iterator;
import java.util.List;

import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;

/**
 *
 * @author  tfc130
 */
public class InfoCurricularCourse  extends InfoObject implements Comparable,ISiteComponent{
	private String name;
	private String code;
	private Double credits;
	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;
	//	private List associatedInfoCurricularSemesters;
	private List infoScopes;
	private List infoAssociatedExecutionCourses;
	private CurricularCourseType type;
	private CurricularCourseExecutionScope curricularCourseExecutionScope;
	private Boolean mandatory;
	private InfoUniversity infoUniversity;
	private Boolean basic;
	
	private String chosen;

    //	Nuno Correia & Ricardo Rodrigues
    private InfoScientificArea scientificArea;

    /**
     * @return
     */
    public Boolean getBasic()
    {
        return basic;
    }

    /**
     * @param basic
     */
    public void setBasic(Boolean basic)
    {
        this.basic = basic;
    }

    public InfoCurricularCourse()
    {
    }

    public String getOwnershipType()
    {
        String result = "";
        if (getBasic().booleanValue())
        {
            result = "Básica";
        }
        else
        {
            result = "Não Básica";
        }
        return result;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoCurricularCourse)
        {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) obj;
            resultado =
                (getName().equals(infoCurricularCourse.getName())
                    && getCode().equals(infoCurricularCourse.getCode()));
        }

        return resultado;
    }

    public String toString()
    {
        String result = "[" + this.getClass().getName() + ": ";
        result += ", nome =" + name;
        result += ", sigla =" + code;
        result += ", type =" + this.type;
        result += ", credits =" + this.credits;
        result += "]\n";
        return result;
    }

    /**
     * Returns the code.
     * @return String
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Returns the nome.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the code.
     * @param code The code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * Sets the nome.
     * @param nome The nome to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the credits.
     * @return Double
     */
    public Double getCredits()
    {
        return credits;
    }

    //	/**
    //	 * Returns the curricularYear.
    //	 * @return Integer
    //	 * @deprecated
    //	 */
    //	public Integer getCurricularYear() {
    //		return curricularYear;
    //	}

    /**
     * Returns the labHours.
     * @return Double
     */
    public Double getLabHours()
    {
        return labHours;
    }

    /**
     * Returns the praticalHours.
     * @return Double
     */
    public Double getPraticalHours()
    {
        return praticalHours;
    }

    //	/**
    //	 * Returns the semester.
    //	 * @return Integer
    //	 * @deprecated
    //	 */
    //	public Integer getSemester() {
    //		return semester;
    //	}

    /**
     * Returns the theoPratHours.
     * @return Double
     */
    public Double getTheoPratHours()
    {
        return theoPratHours;
    }

    /**
     * Returns the theoreticalHours.
     * @return Double
     */
    public Double getTheoreticalHours()
    {
        return theoreticalHours;
    }

    /**
     * Sets the credits.
     * @param credits The credits to set
     */
    public void setCredits(Double credits)
    {
        this.credits = credits;
    }

    //	/**
    //	 * Sets the curricularYear.
    //	 * @param curricularYear The curricularYear to set
    //	 * @deprecated
    //	 */
    //	public void setCurricularYear(Integer curricularYear) {
    //		this.curricularYear = curricularYear;
    //	}

    /**
     * Sets the labHours.
     * @param labHours The labHours to set
     */
    public void setLabHours(Double labHours)
    {
        this.labHours = labHours;
    }

    /**
     * Sets the praticalHours.
     * @param praticalHours The praticalHours to set
     */
    public void setPraticalHours(Double praticalHours)
    {
        this.praticalHours = praticalHours;
    }

    //	/**
    //	 * Sets the semester.
    //	 * @param semester The semester to set
    //	 * @deprecated
    //	 */
    //	public void setSemester(Integer semester) {
    //		this.semester = semester;
    //	}

    /**
     * Sets the theoPratHours.
     * @param theoPratHours The theoPratHours to set
     */
    public void setTheoPratHours(Double theoPratHours)
    {
        this.theoPratHours = theoPratHours;
    }

    /**
     * Sets the theoreticalHours.
     * @param theoreticalHours The theoreticalHours to set
     */
    public void setTheoreticalHours(Double theoreticalHours)
    {
        this.theoreticalHours = theoreticalHours;
    }

    /**
     * Returns the infoDegreeCurricularPlan.
     * @return InfoDegreeCurricularPlan
     */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan()
    {
        return infoDegreeCurricularPlan;
    }

    /**
     * Sets the infoDegreeCurricularPlan.
     * @param infoDegreeCurricularPlan The infoDegreeCurricularPlan to set
     */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan)
    {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    //	/**
    //	 * @return List
    //	 */
    //	public List getAssociatedInfoCurricularSemesters() {
    //		return associatedInfoCurricularSemesters;
    //	}
    //
    //	/**
    //	 * Sets the associatedInfoCurricularSemesters.
    //	 * @param associatedInfoCurricularSemesters The associatedInfoCurricularSemesters to set
    //	 */
    //	public void setAssociatedInfoCurricularSemesters(List associatedInfoCurricularSemesters) {
    //		this.associatedInfoCurricularSemesters = associatedInfoCurricularSemesters;
    //	}

    /**
     * @return List
     */
    public List getInfoScopes()
    {
        return infoScopes;
    }

    /**
     * Sets the infoScopes.
     * @param infoScopes The infoScopes to set
     */
    public void setInfoScopes(List infoScopes)
    {
        this.infoScopes = infoScopes;
    }

    /**
     * @return CurricularCourseType
     */
    public CurricularCourseType getType()
    {
        return type;
    }

    /**
     * Sets the type.
     * @param type The type to set
     */
    public void setType(CurricularCourseType type)
    {
        this.type = type;
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope()
    {
        return curricularCourseExecutionScope;
    }

    public Boolean getMandatory()
    {
        return mandatory;
    }

    public void setCurricularCourseExecutionScope(CurricularCourseExecutionScope scope)
    {
        curricularCourseExecutionScope = scope;
    }

    public void setMandatory(Boolean boolean1)
    {
        mandatory = boolean1;
    }

    public boolean infoCurricularCourseIsMandatory()
    {
        return mandatory.booleanValue();
    }

    public InfoCurricularCourseScope getInfoCurricularCourseScope(
        InfoBranch infoBranch,
        Integer semester)
    {
        InfoCurricularCourseScope infoCurricularCourseScope = null;
        Iterator iterator = this.getInfoScopes().iterator();
        while (iterator.hasNext())
        {
            InfoCurricularCourseScope infoCurricularCourseScope2 =
                (InfoCurricularCourseScope) iterator.next();
            if (infoCurricularCourseScope2.getInfoBranch().equals(infoBranch)
                && infoCurricularCourseScope2.getInfoCurricularSemester().getSemester().equals(semester))
            {
                infoCurricularCourseScope = infoCurricularCourseScope2;
                break;
            }
        }
        return infoCurricularCourseScope;
    }
    public InfoUniversity getInfoUniversity()
    {
        return infoUniversity;
    }

    public void setInfoUniversity(InfoUniversity university)
    {
        this.infoUniversity = university;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0)
    {
        int result = 0;
        if (getMinScope() < ((InfoCurricularCourse) arg0).getMinScope())
        {
            result = -1;
        }
        else if (getMinScope() > ((InfoCurricularCourse) arg0).getMinScope())
        {
            return 1;
        }
        return result;
    }

    private int getMinScope()
    {
        int minScope = 0;
        List scopes = getInfoScopes();
        Iterator iter = scopes.iterator();
        while (iter.hasNext())
        {
            InfoCurricularCourseScope infoScope = (InfoCurricularCourseScope) iter.next();
            if (minScope == 0
                || minScope
                    > infoScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().intValue())
            {
                minScope =
                    infoScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().intValue();
            }
        }

        return minScope;
    }

    public List getInfoAssociatedExecutionCourses()
    {
        return infoAssociatedExecutionCourses;
    }

    public void setInfoAssociatedExecutionCourses(List infoAssociatedExecutionCourses)
    {
        this.infoAssociatedExecutionCourses = infoAssociatedExecutionCourses;
    }

    //	Nuno Correia & Ricardo Rodrigues
    /**
	 * @return
	 */
    public InfoScientificArea getInfoScientificArea()
    {
        return scientificArea;
    }

    /**
     * @param scientificArea
     */
    public void setInfoScientificArea(InfoScientificArea scientificArea)
    {
        this.scientificArea = scientificArea;
    }
	/**
	 * @return Returns the chosen.
	 */
	public String getChosen()
	{
		return chosen;
	}

	/**
	 * @param chosen The chosen to set.
	 */
	public void setChosen(String chosen)
	{
		this.chosen = chosen;
	}

}