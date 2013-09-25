package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class MergePrecedencesForDegreeCurricularPlan {

    @Atomic
    public static void run(String firstPrecedenceID, String secondPrecedenceID) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        if (firstPrecedenceID.equals(secondPrecedenceID)) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        Precedence firstPrecedence = FenixFramework.getDomainObject(firstPrecedenceID);
        Precedence secondPrecedence = FenixFramework.getDomainObject(secondPrecedenceID);

        firstPrecedence.mergePrecedences(secondPrecedence);
    }

}