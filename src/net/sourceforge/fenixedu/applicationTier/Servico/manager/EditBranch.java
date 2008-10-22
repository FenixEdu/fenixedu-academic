/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;

/**
 * @author lmac1
 */

public class EditBranch extends FenixService {

    public void run(InfoBranch infoBranch) throws FenixServiceException {
	Branch branch = rootDomainObject.readBranchByOID(infoBranch.getIdInternal());

	if (branch == null) {
	    throw new NonExistingServiceException();
	}

	branch.edit(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode());
    }

}
