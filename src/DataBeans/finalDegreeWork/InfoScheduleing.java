/*
 * Created on 2004/03/10
 *
 */
package DataBeans.finalDegreeWork;

import java.util.Date;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoObject;

/**
 * @author Luis Cruz
 *
 */
public class InfoScheduleing extends InfoObject {

	private InfoExecutionDegree executionDegree;
	private Date startOfProposalPeriod;
	private Date endOfProposalPeriod;
	private Date startOfCandidacyPeriod;
	private Date endOfCandidacyPeriod;

	/* Construtores */
	public InfoScheduleing() {
		super();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoScheduleing) {
			InfoScheduleing proposal = (InfoScheduleing) obj;

			result = getExecutionDegree().equals(proposal.getExecutionDegree());
		}
		return result;
	}

	public String toString() {
		String result = "[InfoScheduleing";
		result += ", idInternal=" + getIdInternal();
		result += ", InfoDegreeCurricularPlan=" + getExecutionDegree();
		result += "]";
		return result;
	}

	/**
	 * @return Returns the endOfProposalPeriod.
	 */
	public Date getEndOfProposalPeriod() {
		return endOfProposalPeriod;
	}

	/**
	 * @param endOfProposalPeriod The endOfProposalPeriod to set.
	 */
	public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
		this.endOfProposalPeriod = endOfProposalPeriod;
	}

	/**
	 * @return Returns the executionDegree.
	 */
	public InfoExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	/**
	 * @param executionDegree The executionDegree to set.
	 */
	public void setExecutionDegree(InfoExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	/**
	 * @return Returns the startOfProposalPeriod.
	 */
	public Date getStartOfProposalPeriod() {
		return startOfProposalPeriod;
	}

	/**
	 * @param startOfProposalPeriod The startOfProposalPeriod to set.
	 */
	public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
		this.startOfProposalPeriod = startOfProposalPeriod;
	}

    /**
     * @return Returns the startOfCandidacyPeriod.
     */
    public Date getStartOfCandidacyPeriod()
    {
        return startOfCandidacyPeriod;
    }
    /**
     * @param startOfCandidacyPeriod The startOfCandidacyPeriod to set.
     */
    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod)
    {
        this.startOfCandidacyPeriod = startOfCandidacyPeriod;
    }
    /**
     * @return Returns the endOfCandidacyPeriod.
     */
    public Date getEndOfCandidacyPeriod()
    {
        return endOfCandidacyPeriod;
    }
    /**
     * @param endOfCandidacyPeriod The endOfCandidacyPeriod to set.
     */
    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod)
    {
        this.endOfCandidacyPeriod = endOfCandidacyPeriod;
    }
}