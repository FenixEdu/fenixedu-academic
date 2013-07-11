package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;


import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteFunctionalityParameter {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(FunctionalityParameter functionalityParameter) {
        functionalityParameter.delete();
    }

}