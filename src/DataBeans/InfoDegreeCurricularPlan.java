package DataBeans;

import java.io.Serializable;
import java.util.Date;

import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author David Santos
 *
 * 19/Mar/2003
 */

public class InfoDegreeCurricularPlan extends InfoObject implements Serializable {

	private InfoDegree infoDegree;
	private String name;
	private DegreeCurricularPlanState state;
	private Date initialDate;
	private Date endDate;
	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;
	private Double neededCredits;
	private MarkType markType;
	private Integer numerusClausus;

	public InfoDegreeCurricularPlan() {
		setName(null);
		setInfoDegree(null);
		setState(null);
		setInitialDate(null);
		setEndDate(null);
	}

	public InfoDegreeCurricularPlan(String name, InfoDegree infoDegree) {
		this();
		setName(name);
		setInfoDegree(infoDegree);
	}

	public InfoDegreeCurricularPlan(String nome, InfoDegree infoDegree, DegreeCurricularPlanState state, Date initialDate, Date endDate) {
		setName(nome);
		setInfoDegree(infoDegree);
		setState(state);
		setInitialDate(initialDate);
		setEndDate(endDate);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDegreeCurricularPlan) {
			InfoDegreeCurricularPlan infoCurricularPlan = (InfoDegreeCurricularPlan) obj;
			result = (this.getName().equals(infoCurricularPlan.getName()) && this.getInfoDegree().equals(infoCurricularPlan.getInfoDegree()));
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state + "; ";
		result += "NeededCredits = " + this.neededCredits + "; ";
		result += "degree = " + this.infoDegree + "]";
		result += "Numerus Clausus = " + this.numerusClausus + "]";
		return result;
	}

	

	/**
	 * @return Needed Credtis to Finish the Degree
	 */
	public Double getNeededCredits() {
		return neededCredits;
	}

	/**
	 * @param neededCredits
	 */
	public void setNeededCredits(Double neededCredits) {
		this.neededCredits = neededCredits;
	}

	/**
	 * @return Date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return InfoDegree
	 */
	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	/**
	 * @return Date
	 */
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return DegreeCurricularPlanState
	 */
	public DegreeCurricularPlanState getState() {
		return state;
	}

	/**
	 * Sets the endDate.
	 * @param endDate The endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Sets the infoDegree.
	 * @param infoDegree The infoDegree to set
	 */
	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}

	/**
	 * Sets the initialDate.
	 * @param initialDate The initialDate to set
	 */
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(DegreeCurricularPlanState state) {
		this.state = state;
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

	public MarkType getMarkType() {
		return markType;
	}

	public void setMarkType(MarkType type) {
		markType = type;
	}

	/**
	 * @return
	 */
	public Integer getNumerusClausus() {
		return numerusClausus;
	}

	/**
	 * @param integer
	 */
	public void setNumerusClausus(Integer integer) {
		numerusClausus = integer;
	}

}