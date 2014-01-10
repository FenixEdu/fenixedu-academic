/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class EditBranch {

    @Atomic
    public static void run(InfoBranch infoBranch) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Branch branch = FenixFramework.getDomainObject(infoBranch.getExternalId());

        if (branch == null) {
            throw new NonExistingServiceException();
        }

        branch.edit(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode());
    }

}