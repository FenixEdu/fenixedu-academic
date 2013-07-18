/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class EditBranch {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoBranch infoBranch) throws FenixServiceException {
        Branch branch = RootDomainObject.getInstance().readBranchByOID(infoBranch.getIdInternal());

        if (branch == null) {
            throw new NonExistingServiceException();
        }

        branch.edit(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode());
    }

}