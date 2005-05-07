/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * This class contains all the information regarding a Reimbursement Guide. <br>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReimbursementGuide extends ReimbursementGuide_Base {

	protected Calendar creationDate;

	/**
	 * 
	 */
	public ReimbursementGuide() {

	}

	/**
	 * @param reimbursementGuideId
	 */
	public ReimbursementGuide(Integer reimbursementGuideId) {
		setIdInternal(reimbursementGuideId);
	}

	/**
	 * @return
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public IReimbursementGuideSituation getActiveReimbursementGuideSituation() {

		return (IReimbursementGuideSituation) CollectionUtils.find(
				getReimbursementGuideSituations(), new Predicate() {
					public boolean evaluate(Object obj) {
						IReimbursementGuideSituation situation = (IReimbursementGuideSituation) obj;
						return situation.getState().getState().intValue() == State.ACTIVE;
					}
				});

	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IReimbursementGuide) {
			IReimbursementGuide reimbursementGuide = (IReimbursementGuide) obj;

			if ((getNumber() == null && reimbursementGuide.getNumber() == null)
					|| (getNumber().equals(reimbursementGuide.getNumber()))) {
				result = true;
			}
		}

		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += " idInternal=" + getIdInternal();
		result += ", number=" + getNumber();
		result += ", creation Date=" + creationDate;
		result += "]";
		return result;
	}

}