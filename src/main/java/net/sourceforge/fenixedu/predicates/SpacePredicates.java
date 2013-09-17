package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.ExtensionSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class SpacePredicates {

    // Generic Space Predicates

    public static final AccessControlPredicate<Space> checkPermissionsToManageSpace = new AccessControlPredicate<Space>() {
        @Override
        public boolean evaluate(Space space) {
            space.checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getPerson());
            return true;
        }
    };

    public static final AccessControlPredicate<Space> checkIfLoggedPersonIsSpaceAdministrator =
            new AccessControlPredicate<Space>() {
                @Override
                public boolean evaluate(Space space) {
                    space.checkIfLoggedPersonIsSpacesAdministrator(AccessControl.getPerson());
                    return true;
                }
            };

    // Room Classifications Predicates

    public static final AccessControlPredicate<RoomClassification> checkIfLoggedPersonHasPermissionsToManageRoomClassifications =
            new AccessControlPredicate<RoomClassification>() {
                @Override
                public boolean evaluate(RoomClassification roomClassification) {
                    return Space.personIsSpacesAdministrator(AccessControl.getPerson());
                }
            };

    // Room Capacities Predicates

    public static final AccessControlPredicate<Space> checkPermissionsToManageRoomCapacities =
            new AccessControlPredicate<Space>() {
                @Override
                public boolean evaluate(Space space) {
                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSpacesAllocation(AccessControl.getPerson());
                    return true;
                }
            };

    // Space Occupations Predicates

    private static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupations =
            new AccessControlPredicate<SpaceOccupation>() {
                @Override
                public boolean evaluate(SpaceOccupation spaceOccupation) {
                    spaceOccupation.checkPermissionsToManageSpaceOccupations();
                    return true;
                }
            };

    private static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupationsWithoutCheckSpaceManager =
            new AccessControlPredicate<SpaceOccupation>() {
                @Override
                public boolean evaluate(SpaceOccupation spaceOccupation) {
                    spaceOccupation.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManager();
                    return true;
                }
            };

    public static final AccessControlPredicate<GenericEventSpaceOccupation> checkPermissionsToManageGenericEventSpaceOccupations =
            new AccessControlPredicate<GenericEventSpaceOccupation>() {
                @Override
                public boolean evaluate(GenericEventSpaceOccupation spaceOccupation) {
                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSpacesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupations.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<ExtensionSpaceOccupation> checkPermissionsToManageExtensionSpaceOccupations =
            new AccessControlPredicate<ExtensionSpaceOccupation>() {
                @Override
                public boolean evaluate(ExtensionSpaceOccupation spaceOccupation) {
                    return checkPermissionsToManageOccupations.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<UnitSpaceOccupation> checkIfLoggedPersonHasPermissionsToManageUnitSpaceOccupations =
            new AccessControlPredicate<UnitSpaceOccupation>() {
                @Override
                public boolean evaluate(UnitSpaceOccupation unitSpaceOccupation) {
                    return checkPermissionsToManageSpace.evaluate(unitSpaceOccupation.getSpace());
                }
            };

    public static final AccessControlPredicate<PersonSpaceOccupation> checkPermissionsToManagePersonSpaceOccupations =
            new AccessControlPredicate<PersonSpaceOccupation>() {
                @Override
                public boolean evaluate(PersonSpaceOccupation spaceOccupation) {
                    return checkPermissionsToManageOccupations.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<WrittenEvaluationSpaceOccupation> checkPermissionsToManageWrittenEvaluationSpaceOccupations =
            new AccessControlPredicate<WrittenEvaluationSpaceOccupation>() {
                @Override
                public boolean evaluate(WrittenEvaluationSpaceOccupation spaceOccupation) {
                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToManageLessonSpaceOccupations =
            new AccessControlPredicate<LessonSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonSpaceOccupation spaceOccupation) {
                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToDeleteLessonSpaceOccupations =
            new AccessControlPredicate<LessonSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonSpaceOccupation spaceOccupation) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                        return true;
                    }

                    ExecutionCourse executionCourse = spaceOccupation.getLesson().getExecutionCourse();
                    if (loggedPerson.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }

                    return checkPermissionsToManageLessonSpaceOccupations.evaluate(spaceOccupation);
                }
            };

    public static final AccessControlPredicate<LessonInstanceSpaceOccupation> checkPermissionsToManageLessonInstanceSpaceOccupations =
            new AccessControlPredicate<LessonInstanceSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation) {
                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return true;
                }
            };

    public static final AccessControlPredicate<LessonInstanceSpaceOccupation> checkPermissionsToManageLessonInstanceSpaceOccupationsWithTeacherCheck =
            new AccessControlPredicate<LessonInstanceSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                        return true;
                    }

                    if (loggedPerson.getProfessorships().size() > 0) {
                        return true;
                    }

                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(loggedPerson);
                    return true;
                }
            };

    // Space Information Predicates

    public static final AccessControlPredicate<SpaceInformation> checkIfLoggedPersonHasPermissionsToEditSpaceInformation =
            new AccessControlPredicate<SpaceInformation>() {
                @Override
                public boolean evaluate(SpaceInformation spaceInformation) {
                    return checkPermissionsToManageSpace.evaluate(spaceInformation.getSpace());
                }
            };

    public static final AccessControlPredicate<SpaceInformation> checkIfLoggedPersonHasPermissionsToManageSpaceInformation =
            new AccessControlPredicate<SpaceInformation>() {
                @Override
                public boolean evaluate(SpaceInformation spaceInformation) {
                    return checkIfLoggedPersonIsSpaceAdministrator.evaluate(spaceInformation.getSpace());
                }
            };

    // Space Responsibility Predicates

    public static final AccessControlPredicate<SpaceResponsibility> checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits =
            new AccessControlPredicate<SpaceResponsibility>() {
                @Override
                public boolean evaluate(SpaceResponsibility spaceResponsibility) {
                    return checkPermissionsToManageSpace.evaluate(spaceResponsibility.getSpace());
                }
            };

    // Space Blueprints Predicates

    public static final AccessControlPredicate<Blueprint> checkIfLoggedPersonHasPermissionsToManageBlueprints =
            new AccessControlPredicate<Blueprint>() {
                @Override
                public boolean evaluate(Blueprint blueprint) {
                    return checkIfLoggedPersonIsSpaceAdministrator.evaluate(blueprint.getSpace());
                }
            };
}
