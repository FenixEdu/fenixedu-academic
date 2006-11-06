package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;

public class SpacePredicates {

    public static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupations = new AccessControlPredicate<SpaceOccupation>() {
	public boolean evaluate(SpaceOccupation spaceOccupation) {
	    spaceOccupation.checkPermissionsToManageSpaceOccupations();
	    return true;
	}
    };

    public static final AccessControlPredicate<SpaceInformation> checkIfLoggedPersonHasPermissionsToManageSpaceInformation = new AccessControlPredicate<SpaceInformation>() {
	public boolean evaluate(SpaceInformation spaceInformation) {
	    spaceInformation.getSpace().checkIfLoggedPersonHasPermissionsToManageSpace(
		    AccessControl.getUserView().getPerson());
	    return true;
	}
    };
   
    public static final AccessControlPredicate<SpaceResponsibility> checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits = new AccessControlPredicate<SpaceResponsibility>() {
	public boolean evaluate(SpaceResponsibility spaceResponsibility) {
	    spaceResponsibility.getSpace().checkIfLoggedPersonHasPermissionsToManageSpace(
		    AccessControl.getUserView().getPerson());
	    return true;
	}
    };
}
