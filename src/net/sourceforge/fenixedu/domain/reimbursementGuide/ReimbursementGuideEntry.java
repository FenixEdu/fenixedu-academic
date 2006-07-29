/*
 * Created on 18/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br/>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */

public class ReimbursementGuideEntry extends ReimbursementGuideEntry_Base {

	public ReimbursementGuideEntry() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        removeGuideEntry();
        removeReimbursementGuide();
        removeReimbursementTransaction();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
