/*
 * Created on 18/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

/**
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */

public class ReimbursementGuideEntry extends ReimbursementGuideEntry_Base {

	public ReimbursementGuideEntry() {
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