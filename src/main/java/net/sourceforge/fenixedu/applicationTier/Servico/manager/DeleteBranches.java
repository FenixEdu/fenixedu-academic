/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */

public class DeleteBranches {

    // delete a set of branches
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List<String> internalIds, Boolean forceDelete) throws FenixServiceException {
        Iterator<String> iter = internalIds.iterator();

        List undeletedCodes = new ArrayList();
        String internalId;
        Branch branch;

        while (iter.hasNext()) {
            internalId = iter.next();
            branch = AbstractDomainObject.fromExternalId(internalId);
            if (branch != null) {
                try {
                    if (branch.getStudentCurricularPlans().isEmpty()) {
                        branch.delete();
                    } else {
                        if (forceDelete.booleanValue() == true) {
                            branch.delete();
                        } else {
                            undeletedCodes.add(branch.getCode());
                        }
                    }
                } catch (DomainException e) {
                    undeletedCodes.add(branch.getCode());
                }
            }
        }

        return undeletedCodes;
    }
}