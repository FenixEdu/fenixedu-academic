/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author jpvl
 */
public class EnrolmentPeriod extends DomainObject implements IEnrolmentPeriod {
	private Integer keyDegreeCurricularPlan;
	private Integer keyExecutionPeriod;
	
	private IDegreeCurricularPlan degreeCurricularPlan;
	private IExecutionPeriod executionPeriod;
	private Date startDate;
	private Date endDate;

	
	public EnrolmentPeriod(){
		
	}
	
	/* (non-Javadoc)
	 * @see Dominio.IEnrolmentPeriod#setDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	/* (non-Javadoc)
	 * @see Dominio.IEnrolmentPeriod#setExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}

	/* (non-Javadoc)
	 * @see Dominio.IEnrolmentPeriod#setStartDate(java.util.Date)
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/* (non-Javadoc)
	 * @see Dominio.IEnrolmentPeriod#setEndDate(java.util.Date)
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;		
	}

	/**
	 * @return
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}

	/**
	 * @return
	 */
	public Integer getKeyDegreeCurricularPlan() {
		return keyDegreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	/**
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param integer
	 */
	public void setKeyDegreeCurricularPlan(Integer integer) {
		keyDegreeCurricularPlan = integer;
	}

	/**
	 * @param integer
	 */
	public void setKeyExecutionPeriod(Integer integer) {
		keyExecutionPeriod = integer;
	}

}
