package Dominio;

import java.util.Iterator;
import java.util.List;

import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularCourse extends DomainObject implements ICurricularCourse
{
    private Integer departmentCourseKey;
    private Integer degreeCurricularPlanKey;
    private Integer universityKey;
    private Integer keyScientificArea;

    private Double credits;
    private Double theoreticalHours;
    private Double praticalHours;
    private Double theoPratHours;
    private Double labHours;
    private String name;
    private String code;
    private IDisciplinaDepartamento departmentCourse;
    private IDegreeCurricularPlan degreeCurricularPlan;
    private CurricularCourseType type;
    private CurricularCourseExecutionScope curricularCourseExecutionScope;
    private Boolean mandatory;
    private IUniversity university;
    private Boolean basic;
    private List associatedExecutionCourses;
    private List scopes;
    private IScientificArea scientificArea;
    private Integer maximumValueForAcumulatedEnrollments;
    private Integer minimumValueForAcumulatedEnrollments;
    private Integer enrollmentWeigth;
    private Double ectsCredits;

    public CurricularCourse(Integer idInternal)
    {
        setIdInternal(idInternal);

    }

    public CurricularCourse()
    {

        setCode(null);
        setCredits(null);
        setDegreeCurricularPlan(null);
        setDegreeCurricularPlanKey(null);
        setDepartmentCourse(null);
        setDepartmentCourseKey(null);
        setLabHours(null);
        setName(null);
        setPraticalHours(null);
        setTheoPratHours(null);
        setTheoreticalHours(null);
        setAssociatedExecutionCourses(null);
        setScopes(null);
    }

    /**
     * @deprecated
     */
    public CurricularCourse(
        Double credits,
        Double theoreticalHours,
        Double praticalHours,
        Double theoPratHours,
        Double labHours,
        Integer curricularYear,
        Integer semester,
        String name,
        String code,
        IDisciplinaDepartamento departmentCourse,
        IDegreeCurricularPlan degreeCurricularPlan)
    {

        this();
        setCode(code);
        setCredits(credits);
        setDegreeCurricularPlan(degreeCurricularPlan);
        setDepartmentCourse(departmentCourse);
        setLabHours(labHours);
        setName(name);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setTheoreticalHours(theoreticalHours);
    }

    public CurricularCourse(
        Double credits,
        Double theoreticalHours,
        Double praticalHours,
        Double theoPratHours,
        Double labHours,
        String name,
        String code,
        IDisciplinaDepartamento departmentCourse,
        IDegreeCurricularPlan degreeCurricularPlan)
    {

        this();
        setCode(code);
        setCredits(credits);
        setDegreeCurricularPlan(degreeCurricularPlan);
        setDepartmentCourse(departmentCourse);
        setLabHours(labHours);
        setName(name);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setTheoreticalHours(theoreticalHours);
    }

    public CurricularCourse(
        Double credits,
        Double theoreticalHours,
        Double praticalHours,
        Double theoPratHours,
        Double labHours,
        String name,
        String code,
        IDisciplinaDepartamento departmentCourse,
        IDegreeCurricularPlan degreeCurricularPlan,
        CurricularCourseType type)
    {

        this();
        setCode(code);
        setCredits(credits);
        setDegreeCurricularPlan(degreeCurricularPlan);
        setDepartmentCourse(departmentCourse);
        setLabHours(labHours);
        setName(name);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setTheoreticalHours(theoreticalHours);
        setType(type);
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof ICurricularCourse)
        {
            ICurricularCourse dc = (ICurricularCourse) obj;
            resultado =
                getDegreeCurricularPlan().equals(dc.getDegreeCurricularPlan())
                    && getName().equals(dc.getName())
                    && getCode().equals(dc.getCode());
        }
        return resultado;
    }

    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
            .append("[")
            .append(this.getClass())
            .append(":")
            .append("idInternal = ")
            .append(this.getIdInternal())
            .append(";name = ")
            .append(this.name)
            .append(";code = ")
            .append(this.code)
            .append("\n degreeCurricularPlan = ")
            .append(this.getDegreeCurricularPlan())
            .append(";type = ")
            .append(this.type);
        return stringBuffer.toString();
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
     * Returns the credits.
     * @return double
     */
    public Double getCredits()
    {
        return credits;
    }

    /**
     * Returns the degreeCurricularPlan.
     * @return IDegreeCurricularPlan
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan()
    {
        return degreeCurricularPlan;
    }

    /**
     * Returns the degreeCurricularPlanKey.
     * @return int
     */
    public Integer getDegreeCurricularPlanKey()
    {
        return degreeCurricularPlanKey;
    }

    /**
     * Returns the departmentCourse.
     * @return IDisciplinaDepartamento
     */
    public IDisciplinaDepartamento getDepartmentCourse()
    {
        return departmentCourse;
    }

    /**
     * Returns the departmentCourseKey.
     * @return int
     */
    public Integer getDepartmentCourseKey()
    {
        return departmentCourseKey;
    }

    /**
     * Returns the labHours.
     * @return double
     */
    public Double getLabHours()
    {
        return labHours;
    }

    /**
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the praticalHours.
     * @return double
     */
    public Double getPraticalHours()
    {
        return praticalHours;
    }

    /**
     * Returns the theoPratHours.
     * @return double
     */
    public Double getTheoPratHours()
    {
        return theoPratHours;
    }

    /**
     * Returns the theoreticalHours.
     * @return double
     */
    public Double getTheoreticalHours()
    {
        return theoreticalHours;
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
     * Sets the degreeCurricularPlan.
     * @param degreeCurricularPlan The degreeCurricularPlan to set
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
    {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    /**
     * Sets the degreeCurricularPlanKey.
     * @param degreeCurricularPlanKey The degreeCurricularPlanKey to set
     */
    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey)
    {
        this.degreeCurricularPlanKey = degreeCurricularPlanKey;
    }

    /**
     * Sets the departmentCourse.
     * @param departmentCourse The departmentCourse to set
     */
    public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse)
    {
        this.departmentCourse = departmentCourse;
    }

    /**
     * Sets the departmentCourseKey.
     * @param departmentCourseKey The departmentCourseKey to set
     */
    public void setDepartmentCourseKey(Integer departmentCourseKey)
    {
        this.departmentCourseKey = departmentCourseKey;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the associatedExecutionCourses.
     * @return List
     */
    public List getAssociatedExecutionCourses()
    {
        return associatedExecutionCourses;
    }

    /**
     * Sets the associatedExecutionCourses.
     * @param associatedExecutionCourses The associatedExecutionCourses to set
     */
    public void setAssociatedExecutionCourses(List associatedExecutionCourses)
    {
        this.associatedExecutionCourses = associatedExecutionCourses;
    }

    /**
     * Sets the credits.
     * @param credits The credits to set
     */
    public void setCredits(Double credits)
    {
        this.credits = credits;
    }

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
     * @return List
     */
    public List getScopes()
    {
        return scopes;
    }

    /**
     * Sets the scopes.
     * @param scopes The scopes to set
     */
    public void setScopes(List scopes)
    {
        this.scopes = scopes;
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

    public boolean curricularCourseIsMandatory()
    {
        return mandatory.booleanValue();
    }

    public void setCurricularCourseExecutionScope(CurricularCourseExecutionScope scope)
    {
        curricularCourseExecutionScope = scope;
    }

    public void setMandatory(Boolean boolean1)
    {
        mandatory = boolean1;
    }

    public ICurricularCourseScope getCurricularCourseScope(IBranch branch, Integer curricularSemester)
    {
        ICurricularCourseScope curricularCourseScope = null;
        Iterator iterator = this.getScopes().iterator();
        while (iterator.hasNext())
        {
            ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) iterator.next();
            if (curricularCourseScope2.getBranch().equals(branch)
                && curricularCourseScope2.getCurricularSemester().getSemester().equals(curricularSemester))
            {
                curricularCourseScope = curricularCourseScope2;
                break;
            }
        }
        return curricularCourseScope;
    }
    public IUniversity getUniversity()
    {
        return university;
    }

    public Integer getUniversityKey()
    {
        return universityKey;
    }

    public void setUniversity(IUniversity code)
    {
        university = code;
    }

    public void setUniversityKey(Integer integer)
    {
        universityKey = integer;
    }


	//	Nuno Correia & Ricardo Rodrigues
    /**
	 * @return
	 */
    public Integer getKeyScientificArea()
    {
        return keyScientificArea;
    }

    /**
     * @param keyScientificArea
     */
    public void setKeyScientificArea(Integer keyScientificArea)
    {
        this.keyScientificArea = keyScientificArea;
    }

    /**
     * @return
     */
    public IScientificArea getScientificArea()
    {
        return scientificArea;
    }

    /**
     * @param scientificArea
     */
    public void setScientificArea(IScientificArea scientificArea)
    {
        this.scientificArea = scientificArea;
    }

    public ICurricularYear getCurricularYearByBranch(IBranch branch)
    {
    	if(this.getScopes().size() == 1)
    	{
    	return ((ICurricularCourseScope) this.getScopes().get(0)).getCurricularSemester().getCurricularYear();
    	} else
    	{
    		Iterator iterator = this.getScopes().iterator();
    		while(iterator.hasNext())
    		{
    			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
    			if(curricularCourseScope.getBranch().equals(branch))
    			{
    				return curricularCourseScope.getCurricularSemester().getCurricularYear();
				}
			}
		}
		return ((ICurricularCourseScope) this.getScopes().get(0)).getCurricularSemester().getCurricularYear();
	}

    /**
	 * @return Returns the ectsCredits.
	 */
	public Double getEctsCredits()
	{
		return ectsCredits;
	}

	/**
	 * @param ectsCredits The ectsCredits to set.
	 */
	public void setEctsCredits(Double ectsCredits)
	{
		this.ectsCredits = ectsCredits;
	}

	/**
	 * @return Returns the enrollmentWeigth.
	 */
	public Integer getEnrollmentWeigth()
	{
		return enrollmentWeigth;
	}

	/**
	 * @param enrollmentWeigth The enrollmentWeigth to set.
	 */
	public void setEnrollmentWeigth(Integer enrollmentWeigth)
	{
		this.enrollmentWeigth = enrollmentWeigth;
	}

	/**
	 * @return Returns the maximumValueForAcumulatedEnrollments.
	 */
	public Integer getMaximumValueForAcumulatedEnrollments()
	{
		return maximumValueForAcumulatedEnrollments;
	}

	/**
	 * @param maximumValueForAcumulatedEnrollments The maximumValueForAcumulatedEnrollments to set.
	 */
	public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments)
	{
		this.maximumValueForAcumulatedEnrollments = maximumValueForAcumulatedEnrollments;
	}

	/**
	 * @return Returns the minimumValueForAcumulatedEnrollments.
	 */
	public Integer getMinimumValueForAcumulatedEnrollments()
	{
		return minimumValueForAcumulatedEnrollments;
	}

	/**
	 * @param minimumValueForAcumulatedEnrollments The minimumValueForAcumulatedEnrollments to set.
	 */
	public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments)
	{
		this.minimumValueForAcumulatedEnrollments = minimumValueForAcumulatedEnrollments;
	}

}