package Dominio;

import java.util.Date;
import java.util.List;

import Dominio.degree.enrollment.rules.EnrollmentRulesFactory;
import Util.DegreeCurricularPlanState;
import Util.MarkType;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlan extends DomainObject implements IDegreeCurricularPlan {

	private Integer degreeKey;
	
	private ICurso degree;
	private String name;
	private DegreeCurricularPlanState state;
	private Date initialDate;
	private Date endDate;
	private Integer degreeDuration;
	private Double neededCredits;
	private MarkType markType;
	private Integer numerusClausus;
	private String description;
	private String descriptionEn;

    // For enrollment purposes
	private Integer minimalYearForOptionalCourses;
	private String enrollmentStrategyClassName;
	
	private List curricularCourses;
	
	public DegreeCurricularPlan() {
	}

	public DegreeCurricularPlan(Integer idInternal) {
		setIdInternal(idInternal);
		}
		
	public DegreeCurricularPlan(String nome, ICurso degree) {
		setName(nome);
		setDegree(degree);
		setState(null);
		setInitialDate(null);
		setEndDate(null);
	}

	public DegreeCurricularPlan(
		String nome,
		ICurso degree,
		DegreeCurricularPlanState state,
		Date initialDate,
		Date endDate) {
		this();
		setName(nome);
		setDegree(degree);
		setState(state);
		setInitialDate(initialDate);
		setEndDate(endDate);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IDegreeCurricularPlan) {
			IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) obj;
			resultado =
				this.getName().equals(dcp.getName())
					&& this.getDegree().equals(dcp.getDegree());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state + "; ";
		result += "needed Credits = " + this.neededCredits + "; ";
		result += "Mark Type = " + this.markType+ "; ";
		result += "degree = " + this.degree + "]\n";
		result += "NumerusClausus = " + this.numerusClausus + "]\n";
		
		return result;
	}

	

	/**
	 * @return MarkType
	 */
	public MarkType getMarkType() {
		return markType;
	}

	/**
	 * @param markType
	 */
	public void setMarkType(MarkType markType) {
		this.markType = markType;
	}

	/**
	 * @return The Needed Credits to finish the degree
	 */
	public Double getNeededCredits() {
		return neededCredits;
	}

	/**
	 * @param needed Credits to finish the degree
	 */
	public void setNeededCredits(Double neededCredits) {
		this.neededCredits = neededCredits;
	}


	/**
	 * Returns the degreeKey.
	 * @return Integer
	 */
	public Integer getDegreeKey() {
		return degreeKey;
	}

	/**
	 * Returns the degree.
	 * @return ICurso
	 */
	public ICurso getDegree() {
		return degree;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the degreeKey.
	 * @param degreeKey The degreeKey to set
	 */
	public void setDegreeKey(Integer chaveCurso) {
		this.degreeKey = chaveCurso;
	}

	/**
	 * Sets the degree.
	 * @param degree The degree to set
	 */
	public void setDegree(ICurso curso) {
		this.degree = curso;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String nome) {
		this.name = nome;
	}

	/**
	 * @return DegreeCurricularPlanState
	 */
	public DegreeCurricularPlanState getState() {
		return state;
	}

	/**
	 * @return Date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return Date
	 */
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(DegreeCurricularPlanState state) {
		this.state = state;
	}

	/**
	 * Sets the endDate.
	 * @param endDate The endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Sets the initialDate.
	 * @param initialDate The initialDate to set
	 */
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	/* (non-Javadoc)
	 * @see Dominio.IDegreeCurricularPlan#getCurricularCourses()
	 */
	public List getCurricularCourses() {
		return this.curricularCourses;
	}

	/* (non-Javadoc)
	 * @see Dominio.IDegreeCurricularPlan#setCurricularCourses(java.util.List)
	 */
	public void setCurricularCourses(List curricularCourses) {
		this.curricularCourses = curricularCourses;		
	}

	public Integer getDegreeDuration() {
		return degreeDuration;
	}

	public Integer getMinimalYearForOptionalCourses() {
		return minimalYearForOptionalCourses;
	}

	public void setDegreeDuration(Integer integer) {
		degreeDuration = integer;
	}

	public void setMinimalYearForOptionalCourses(Integer integer) {
		minimalYearForOptionalCourses = integer;
	}

	public Integer getNumerusClausus() {
		return numerusClausus;
	}

	public void setNumerusClausus(Integer integer) {
		numerusClausus = integer;
	}

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescriptionEn()
    {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn)
    {
        this.descriptionEn = descriptionEn;
    }

    /**
	 * @return Returns the enrollmentStrategyClassName.
	 */
	public String getEnrollmentStrategyClassName()
	{
		return enrollmentStrategyClassName;
	}

	/**
	 * @param enrollmentStrategyClassName The enrollmentStrategyClassName to set.
	 */
	public void setEnrollmentStrategyClassName(String enrollmentStrategyClassName)
	{
		this.enrollmentStrategyClassName = enrollmentStrategyClassName;
	}

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------

	public List getListOfEnrollmentRules(EnrollmentRuleType enrollmentRuleType)
	{
		return EnrollmentRulesFactory.getInstance().getListOfEnrollmentRules(this, enrollmentRuleType);
	}

	public List getCurricularCoursesFromArea(IBranch area)
	{
		return null;
	}

	public List getCommonAreas()
	{
		return null;
	}

	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------

}