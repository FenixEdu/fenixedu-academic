package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class DeleteDegrees {

    // delete a set of degrees
    @Atomic
    public static List<String> run(List<String> degreesInternalIds) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Iterator<String> iter = degreesInternalIds.iterator();

        List<String> undeletedDegreesNames = new ArrayList<String>();
        while (iter.hasNext()) {
            String internalId = iter.next();
            Degree degree = FenixFramework.getDomainObject(internalId);

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