package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;


import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteFunctionalityParameter {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(FunctionalityParameter functionalityParameter) {
        functionalityParameter.delete();
    }

}