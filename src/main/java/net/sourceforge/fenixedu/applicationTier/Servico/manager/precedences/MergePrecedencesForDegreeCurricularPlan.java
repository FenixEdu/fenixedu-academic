package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class MergePrecedencesForDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(String firstPrecedenceID, String secondPrecedenceID) throws FenixServiceException {

        if (firstPrecedenceID.equals(secondPrecedenceID)) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        Precedence firstPrecedence = FenixFramework.getDomainObject(firstPrecedenceID);
        Precedence secondPrecedence = FenixFramework.getDomainObject(secondPrecedenceID);

        firstPrecedence.mergePrecedences(secondPrecedence);
    }

}