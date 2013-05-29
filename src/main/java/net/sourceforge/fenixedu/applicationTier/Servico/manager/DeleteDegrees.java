package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */

public class DeleteDegrees {

    // delete a set of degrees
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List<String> run(List<String> degreesInternalIds) throws FenixServiceException {
        Iterator<String> iter = degreesInternalIds.iterator();

        List<String> undeletedDegreesNames = new ArrayList<String>();
        while (iter.hasNext()) {
            String internalId = iter.next();
            Degree degree = AbstractDomainObject.fromExternalId(internalId);

            if (degree != null) {

                try {
                    degree.delete();
                } catch (DomainException e) {
                    undeletedDegreesNames.add(degree.getNome());
                }
            }
        }

        return undeletedDegreesNames;
    }

}