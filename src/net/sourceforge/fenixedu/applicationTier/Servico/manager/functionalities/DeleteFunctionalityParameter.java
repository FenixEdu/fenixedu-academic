package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;

public class DeleteFunctionalityParameter extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(FunctionalityParameter functionalityParameter) {
	functionalityParameter.delete();
    }

}