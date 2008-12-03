package net.sourceforge.fenixedu.applicationTier.Servico.space;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;

public class DeletePersonSpaceOccupation extends FenixService {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public static Boolean run(PersonSpaceOccupation personSpaceOccupation) {
	if (personSpaceOccupation != null) {
	    personSpaceOccupation.delete();
	}
	return true;
    }
}