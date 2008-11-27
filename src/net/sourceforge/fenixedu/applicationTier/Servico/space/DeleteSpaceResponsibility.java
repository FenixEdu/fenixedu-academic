package net.sourceforge.fenixedu.applicationTier.Servico.space;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;

public class DeleteSpaceResponsibility extends FenixService {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public static boolean run(SpaceResponsibility spaceResponsibility) {
	if (spaceResponsibility != null) {
	    spaceResponsibility.delete();
	}
	return true;
    }
}