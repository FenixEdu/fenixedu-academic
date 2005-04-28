/*
 * Created on 13/Nov/2003
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 13/Nov/2003
 * 
 */
public class ReimbursementGuideSituation extends
		ReimbursementGuideSituation_Base {

	protected State state;

	protected IReimbursementGuide reimbursementGuide;

	protected IEmployee employee;

	protected Calendar modificationDate;

	protected Calendar officialDate;

	protected ReimbursementGuideState reimbursementGuideState;

	/**
	 * 
	 */
	public ReimbursementGuideSituation() {
	}

	/**
	 * @return
	 */
	public IEmployee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 */
	public void setEmployee(IEmployee employee) {
		this.employee = employee;
	}

	/**
	 * @return
	 */
	public Calendar getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate
	 */
	public void setModificationDate(Calendar modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * @return
	 */
	public IReimbursementGuide getReimbursementGuide() {
		return reimbursementGuide;
	}

	/**
	 * @param reimbursementGuide
	 */
	public void setReimbursementGuide(IReimbursementGuide reimbursementGuide) {
		this.reimbursementGuide = reimbursementGuide;
	}

	/**
	 * @return
	 */
	public ReimbursementGuideState getReimbursementGuideState() {
		return reimbursementGuideState;
	}

	/**
	 * @param reimbursementGuideState
	 */
	public void setReimbursementGuideState(
			ReimbursementGuideState reimbursementGuideState) {
		this.reimbursementGuideState = reimbursementGuideState;
	}

	/**
	 * @return
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return Returns the officialDate.
	 */
	public Calendar getOfficialDate() {
		return officialDate;
	}

	/**
	 * @param officialDate
	 *            The officialDate to set.
	 */
	public void setOfficialDate(Calendar officialDate) {
		this.officialDate = officialDate;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IReimbursementGuideSituation) {
			IReimbursementGuideSituation reimbursementGuideSituation = (IReimbursementGuideSituation) obj;

			if ((this.getIdInternal() == null && reimbursementGuideSituation
					.getIdInternal() == null)
					|| (this.getIdInternal().equals(reimbursementGuideSituation
							.getIdInternal()))) {
				result = true;
			}
		}

		return result;
	}

}