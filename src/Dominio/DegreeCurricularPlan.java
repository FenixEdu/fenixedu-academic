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

	private List curricularCourses;
	private Integer idInternal;
	private Integer keyDegree;
	private ICurso degree;
	private String name;
	private DegreeCurricularPlanState state;
	private Date initialDate;
	private Date endDate;
	private List enrolmentInfo;

	public DegreeCurricularPlan() {
	}

	public DegreeCurricularPlan(String nome, ICurso degree) {
		setName(nome);
		setDegree(degree);
		setState(null);
		setInitialDate(null);
		setEndDate(null);
		setEnrolmentInfo(null);
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

	public List getEnrolmentInfo() {
		return enrolmentInfo;	
	}

	public void setEnrolmentInfo(List list) {
		enrolmentInfo = list;
	}
	
	public IDegreeCurricularPlanEnrolmentInfo getDegreeCurricularPlanEnrolmentInfo(){
		if(enrolmentInfo.isEmpty()){
			return null;
		}else{
			return (IDegreeCurricularPlanEnrolmentInfo) enrolmentInfo.get(0);
		}
	}

	public void setDegreeCurricularPlanEnrolmentInfo(IDegreeCurricularPlanEnrolmentInfo degreeCurricularPlanEnrolmentInfo) {
		enrolmentInfo.set(0, degreeCurricularPlanEnrolmentInfo);
	}
}