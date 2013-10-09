package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeletePrecedenceFromDegreeCurricularPlan {

    @Atomic
    public static void run(String precedenceID) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Precedence precedence = FenixFramework.getDomainObject(precedenceID);

        if (precedence != null) {
            precedence.delete();
        }
    }
}