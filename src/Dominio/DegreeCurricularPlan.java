package Dominio;

import java.util.Date;

import Util.DegreeState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlan implements IDegreeCurricularPlan {

	private Integer idInternal;
	private Integer keyDegree;
	private ICurso degree;
	private String name;
	private DegreeState state;
	private Date initialDate;
	private Date endDate;

	public DegreeCurricularPlan() {
	}

	public DegreeCurricularPlan(String nome, ICurso licenciatura) {
		setName(nome);
		setDegree(licenciatura);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof DegreeCurricularPlan) {
			DegreeCurricularPlan dcp = (DegreeCurricularPlan) obj;
			resultado = this.getName().equals(dcp.getName()) && this.getDegree().equals(dcp.getDegree());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.idInternal + "; ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state.toString() + "; ";
		result += "degree = " + this.degree.toString() + "]";
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
	 * Returns the keyDegree.
	 * @return Integer
	 */
	public Integer getKeyDegree() {
		return keyDegree;
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
	 * Sets the keyDegree.
	 * @param keyDegree The keyDegree to set
	 */
	public void setKeyDegree(Integer chaveCurso) {
		this.keyDegree = chaveCurso;
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
	 * @return DegreeState
	 */
	public DegreeState getState() {
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
	public void setState(DegreeState state) {
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

}