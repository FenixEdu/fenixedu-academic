/*
 * Created on Mar 7, 2004
 *
 */
package Dominio.finalDegreeWork;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ICursoExecucao;

/**
 * @author Luis Cruz
 *
 */
public class Scheduleing extends DomainObject implements IScheduleing {

	private Integer keyExecutionDegree;
	private ICursoExecucao executionDegree;
	private Date startOfProposalPeriod;
	private Date endOfProposalPeriod;
	private Integer currentProposalNumber;

	/* Construtores */
	public Scheduleing() {
		super();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IScheduleing) {
			IScheduleing scheduleing = (IScheduleing) obj;

			if (getExecutionDegree() != null && scheduleing != null)
			{	
				result = getExecutionDegree().equals(scheduleing.getExecutionDegree());
			}
		}
		return result;
	}

	public String toString() {
		String result = "[Proposal";
		result += ", idInternal=" + getIdInternal();
		result += ", executionDegree=" + getExecutionDegree();
		result += "]";
		return result;
	}

	/**
	 * @return Returns the executionDegree.
	 */
	public ICursoExecucao getExecutionDegree()
	{
		return executionDegree;
	}

	/**
	 * @param executionDegree The executionDegree to set.
	 */
	public void setExecutionDegree(ICursoExecucao executionDegree)
	{
		this.executionDegree = executionDegree;
	}

	/**
	 * @return Returns the keyExecutionDegree.
	 */
	public Integer getKeyExecutionDegree()
	{
		return keyExecutionDegree;
	}

	/**
	 * @param keyExecutionDegree The keyExecutionDegree to set.
	 */
	public void setKeyExecutionDegree(Integer keyExecutionDegree)
	{
		this.keyExecutionDegree = keyExecutionDegree;
	}

	/**
	 * @return Returns the endOfProposalPeriod.
	 */
	public Date getEndOfProposalPeriod()
	{
		return endOfProposalPeriod;
	}

	/**
	 * @param endOfProposalPeriod The endOfProposalPeriod to set.
	 */
	public void setEndOfProposalPeriod(Date endOfProposalPeriod)
	{
		this.endOfProposalPeriod = endOfProposalPeriod;
	}

	/**
	 * @return Returns the startOfProposalPeriod.
	 */
	public Date getStartOfProposalPeriod()
	{
		return startOfProposalPeriod;
	}

	/**
	 * @param startOfProposalPeriod The startOfProposalPeriod to set.
	 */
	public void setStartOfProposalPeriod(Date startOfProposalPeriod)
	{
		this.startOfProposalPeriod = startOfProposalPeriod;
	}

	/**
	 * @return Returns the currentProposalNumber.
	 */
	public Integer getCurrentProposalNumber() {
		return currentProposalNumber;
	}

	/**
	 * @param currentProposalNumber The currentProposalNumber to set.
	 */
	public void setCurrentProposalNumber(Integer currentProposalNumber) {
		this.currentProposalNumber = currentProposalNumber;
	}

}