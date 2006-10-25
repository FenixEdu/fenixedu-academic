package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.space.SpaceOccupation;

public class SpaceOccupationsPredicates {

    public static final AccessControlPredicate<SpaceOccupation> permissionsToMakeOperations = new AccessControlPredicate<SpaceOccupation>() {
	public boolean evaluate(SpaceOccupation spaceOccupation) {
	    spaceOccupation.checkPermissionsToMakeOperations();
	    return true;
	}
    };
}
