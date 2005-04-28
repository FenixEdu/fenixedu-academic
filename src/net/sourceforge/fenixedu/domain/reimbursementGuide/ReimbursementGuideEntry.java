/*
 * Created on 18/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import net.sourceforge.fenixedu.domain.IGuideEntry;

/**
 * 
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

public class ReimbursementGuideEntry extends ReimbursementGuideEntry_Base {

	protected IGuideEntry guideEntry;

	protected IReimbursementGuide reimbursementGuide;

	public ReimbursementGuideEntry() {
	}

	/**
	 * @return Returns the guideEntry.
	 */
	public IGuideEntry getGuideEntry() {
		return guideEntry;
	}

	/**
	 * @param guideEntry
	 *            The guideEntry to set.
	 */
	public void setGuideEntry(IGuideEntry guideEntry) {
		this.guideEntry = guideEntry;
	}

	/**
	 * @return Returns the reimbursementGuide.
	 */
	public IReimbursementGuide getReimbursementGuide() {
		return reimbursementGuide;
	}

	/**
	 * @param reimbursementGuide
	 *            The reimbursementGuide to set.
	 */
	public void setReimbursementGuide(IReimbursementGuide reimbursementGuide) {
		this.reimbursementGuide = reimbursementGuide;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IReimbursementGuideEntry) {
			IReimbursementGuideEntry reimbursementGuideEntry = (IReimbursementGuideEntry) obj;

			if ((this.getIdInternal() == null && reimbursementGuideEntry
					.getGuideEntry() == null)
					|| (this.getIdInternal().equals(reimbursementGuideEntry
							.getIdInternal()))) {
				result = true;
			}
		}

		return result;
	}

}