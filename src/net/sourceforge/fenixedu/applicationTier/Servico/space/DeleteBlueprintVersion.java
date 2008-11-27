package net.sourceforge.fenixedu.applicationTier.Servico.space;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;

public class DeleteBlueprintVersion extends FenixService {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public static void run(Blueprint blueprint) {
	if (blueprint == null) {
	    throw new DomainException("error.delete.blueprint.no.blueprint");
	}
	blueprint.delete();
    }
}