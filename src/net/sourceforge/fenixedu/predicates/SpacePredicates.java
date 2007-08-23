package net.sourceforge.fenixedu.predicates;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class SpacePredicates {

    public static final AccessControlPredicate<Space> checkPermissionsToManageSpace = new AccessControlPredicate<Space>() {
	public boolean evaluate(Space space) {
	    space.checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getPerson());
	    return true;
	}
    };
    
    public static final AccessControlPredicate<Space> checkIfLoggedPersonIsSpaceAdministrator = new AccessControlPredicate<Space>() {
	public boolean evaluate(Space space) {
	    space.checkIfLoggedPersonIsSpacesAdministrator(AccessControl.getPerson());
	    return true;
	}
    };
    
    public static final AccessControlPredicate<RoomClassification> checkIfLoggedPersonHasPermissionsToManageRoomClassifications = new AccessControlPredicate<RoomClassification>() {
	public boolean evaluate(RoomClassification roomClassification) {
	    return Space.personIsSpacesAdministrator(AccessControl.getPerson());	    
	}
    };    
    
    public static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupations = new AccessControlPredicate<SpaceOccupation>() {
	public boolean evaluate(SpaceOccupation spaceOccupation) {
	    spaceOccupation.checkPermissionsToManageSpaceOccupations();
	    return true;
	}
    };
    
    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToManageLessonSpaceOccupationsWithoutCheckSpaceManagerRole = new AccessControlPredicate<LessonSpaceOccupation>() {
	public boolean evaluate(LessonSpaceOccupation spaceOccupation) {
	    Person loggedPerson = AccessControl.getPerson();
	    ExecutionCourse executionCourse = spaceOccupation.getLesson().getShift().getExecutionCourse();
	    List<Professorship> professorships = executionCourse.getProfessorships();
	    for (Professorship professorship : professorships) {
		if(professorship.getTeacher().getPerson().equals(loggedPerson)) {
		    return true;
		}
	    }
	    if(loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
		return true;
	    }
	    return checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole.evaluate(spaceOccupation);
	}
    };
    
    public static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole = new AccessControlPredicate<SpaceOccupation>() {
	public boolean evaluate(SpaceOccupation spaceOccupation) {
	    spaceOccupation.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole();
	    return true;
	}
    };

    public static final AccessControlPredicate<SpaceInformation> checkIfLoggedPersonHasPermissionsToEditSpaceInformation = new AccessControlPredicate<SpaceInformation>() {
	public boolean evaluate(SpaceInformation spaceInformation) {
	    spaceInformation.getSpace().checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getPerson());
	    return true;
	}
    };
   
    public static final AccessControlPredicate<SpaceInformation> checkIfLoggedPersonHasPermissionsToManageSpaceInformation = new AccessControlPredicate<SpaceInformation>() {
	public boolean evaluate(SpaceInformation spaceInformation) {
	    spaceInformation.getSpace().checkIfLoggedPersonIsSpacesAdministrator(AccessControl.getPerson());
	    return true;	    
	}
    };
    
    public static final AccessControlPredicate<SpaceResponsibility> checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits = new AccessControlPredicate<SpaceResponsibility>() {
	public boolean evaluate(SpaceResponsibility spaceResponsibility) {
	    spaceResponsibility.getSpace().checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getPerson());
	    return true;
	}
    };
    
    public static final AccessControlPredicate<Blueprint> checkIfLoggedPersonHasPermissionsToManageBlueprints = new AccessControlPredicate<Blueprint>() {
	public boolean evaluate(Blueprint blueprint) {
	    blueprint.getSpace().checkIfLoggedPersonIsSpacesAdministrator(AccessControl.getPerson());
	    return true;
	}
    };    
    
    public static final AccessControlPredicate<UnitSpaceOccupation> checkIfLoggedPersonHasPermissionsToManageUnitSpaceOccupations = new AccessControlPredicate<UnitSpaceOccupation>() {
	public boolean evaluate(UnitSpaceOccupation unitSpaceOccupation) {
	    unitSpaceOccupation.getSpace().checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getPerson());
	    return true;
	}
    };
}
