package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteFunctionalityParameter {

    @Atomic
    public static void run(FunctionalityParameter functionalityParameter) {
        check(RolePredicates.MANAGER_PREDICATE);
        functionalityParameter.delete();
    }

}