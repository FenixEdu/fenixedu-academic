package Dominio;

import java.util.Date;
import java.util.List;

import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlan implements IDegreeCurricularPlan {

	private Integer idInternal;
	private Integer degreeKey;
	
	private ICurso degree;
	private String name;
	private DegreeCurricularPlanState state;
	private Date initialDate;
	private Date endDate;
	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;
	
	private List curricularCourses;
	
	public DegreeCurricularPlan() {
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
		if (obj instanceof DegreeCurricularPlan) {
			DegreeCurricularPlan dcp = (DegreeCurricularPlan) obj;
			resultado =
				this.getName().equals(dcp.getName())
					&& this.getDegree().equals(dcp.getDegree());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.idInternal + "; ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state + "; ";
		result += "degree = " + this.degree + "]\n";
		return result;
	}

	/**
	 * Returns the idInternal.
	 * @return Integer
	 */
	public Integer getIdInternal() {
		return idInternal;
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
	 * Sets the idInternal.
	 * @param idInternal The idInternal to set
	 */
	public void setIdInternal(Integer codigoInterno) {
		this.idInternal = codigoInterno;
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

}