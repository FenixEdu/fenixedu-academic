package Dominio;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.degree.enrollment.rules.EnrollmentRulesFactory;
import Util.AreaType;
import Util.BranchType;
import Util.DegreeCurricularPlanState;
import Util.MarkType;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlan extends DomainObject implements IDegreeCurricularPlan
{
	protected Integer degreeKey;

	protected String ojbConcreteClass;
	protected String concreteClassForStudentCurricularPlans;
	protected ICurso degree;
	protected String name;
	protected DegreeCurricularPlanState state;
	protected Date initialDate;
	protected Date endDate;
	protected Integer degreeDuration;
	protected Double neededCredits;
	protected MarkType markType;
	protected Integer numerusClausus;
	protected String description;
	protected String descriptionEn;
	protected List curricularCourses;
	protected List areas;

	// For enrollment purposes
	protected Integer minimalYearForOptionalCourses;
	protected String enrollmentStrategyClassName;

	public DegreeCurricularPlan()
	{
		ojbConcreteClass = getClass().getName();
	}

	public IStudentCurricularPlan getNewStudentCurricularPlan()
	{
		IStudentCurricularPlan studentCurricularPlan = null;

		try
		{
			Class classDefinition = Class.forName(getConcreteClassForStudentCurricularPlans());
			studentCurricularPlan = (IStudentCurricularPlan) classDefinition.newInstance();
		} catch (InstantiationException e)
		{
			System.out.println(e);
		} catch (IllegalAccessException e)
		{
			System.out.println(e);
		} catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		
		return studentCurricularPlan;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;

		if (obj instanceof IDegreeCurricularPlan)
		{
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;

			result = getDegree().equals(degreeCurricularPlan.getDegree())
				&& getName().equals(degreeCurricularPlan.getName());
		}

		return result;
	}

	public String toString()
	{
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state + "; ";
		result += "needed Credits = " + this.neededCredits + "; ";
		result += "Mark Type = " + this.markType + "; ";
		result += "degree = " + this.degree + "]\n";
		result += "NumerusClausus = " + this.numerusClausus + "]\n";

		return result;
	}

	public List getAreas()
	{
		return areas;
	}

	public List getCurricularCourses()
	{
		return curricularCourses;
	}
	
	public ICurso getDegree()
	{
		return degree;
	}
	
	public Integer getDegreeDuration()
	{
		return degreeDuration;
	}
	
	public Integer getDegreeKey()
	{
		return degreeKey;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getDescriptionEn()
	{
		return descriptionEn;
	}
	
	public Date getEndDate()
	{
		return endDate;
	}
	
	public Date getInitialDate()
	{
		return initialDate;
	}
	
	public MarkType getMarkType()
	{
		return markType;
	}
	
	public Integer getMinimalYearForOptionalCourses()
	{
		return minimalYearForOptionalCourses;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Double getNeededCredits()
	{
		return neededCredits;
	}
	
	public Integer getNumerusClausus()
	{
		return numerusClausus;
	}
	
	public DegreeCurricularPlanState getState()
	{
		return state;
	}
	
	public void setAreas(List areas)
	{
		this.areas = areas;
	}
	
	public void setCurricularCourses(List curricularCourses)
	{
		this.curricularCourses = curricularCourses;
	}
	
	public void setDegree(ICurso degree)
	{
		this.degree = degree;
	}
	
	public void setDegreeDuration(Integer degreeDuration)
	{
		this.degreeDuration = degreeDuration;
	}
	
	public void setDegreeKey(Integer degreeKey)
	{
		this.degreeKey = degreeKey;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setDescriptionEn(String descriptionEn)
	{
		this.descriptionEn = descriptionEn;
	}
	
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public void setInitialDate(Date initialDate)
	{
		this.initialDate = initialDate;
	}
	
	public void setMarkType(MarkType markType)
	{
		this.markType = markType;
	}
	
	public void setMinimalYearForOptionalCourses(Integer minimalYearForOptionalCourses)
	{
		this.minimalYearForOptionalCourses = minimalYearForOptionalCourses;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setNeededCredits(Double neededCredits)
	{
		this.neededCredits = neededCredits;
	}
	
	public void setNumerusClausus(Integer numerusClausus)
	{
		this.numerusClausus = numerusClausus;
	}
	
	public void setState(DegreeCurricularPlanState state)
	{
		this.state = state;
	}

	public String getConcreteClassForStudentCurricularPlans()
	{
		return concreteClassForStudentCurricularPlans;
	}

	public String getOjbConcreteClass()
	{
		return ojbConcreteClass;
	}
	
	public void setConcreteClassForStudentCurricularPlans(String concreteClassForStudentCurricularPlans)
	{
		this.concreteClassForStudentCurricularPlans = concreteClassForStudentCurricularPlans;
	}
	
	public void setOjbConcreteClass(String ojbConcreteClass)
	{
		this.ojbConcreteClass = ojbConcreteClass;
	}


	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------

	public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
		EnrollmentRuleType enrollmentRuleType)
	{
		return EnrollmentRulesFactory.getInstance().getListOfEnrollmentRules(this, studentCurricularPlan, executionPeriod,
			enrollmentRuleType);
	}

	public List getCurricularCoursesFromArea(IBranch area, AreaType areaType)
	{
		return EnrollmentRulesFactory.getInstance().getCurricularCoursesFromArea(this, area, areaType);
	}

	public List getCommonAreas()
	{
		return (List) CollectionUtils.select(this.areas, new Predicate()
		{
			public boolean evaluate(Object obj)
			{
				IBranch branch = (IBranch) obj;
				return branch.getBranchType().equals(BranchType.COMMON_BRANCH);
			}
		});
	}

	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------

}