package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class SpacePredicates {

    private static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupationsWithoutCheckSpaceManager =
            new AccessControlPredicate<SpaceOccupation>() {
                @Override
                public boolean evaluate(SpaceOccupation spaceOccupation) {
                    spaceOccupation.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManager();
                    return true;
                }
            };
    public static final AccessControlPredicate<WrittenEvaluationSpaceOccupation> checkPermissionsToManageWrittenEvaluationSpaceOccupations =
            new AccessControlPredicate<WrittenEvaluationSpaceOccupation>() {
                @Override
                public boolean evaluate(WrittenEvaluationSpaceOccupation spaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };
//
    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToManageLessonSpaceOccupations =
            new AccessControlPredicate<LessonSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonSpaceOccupation spaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };
//
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
//
    public static final AccessControlPredicate<LessonInstanceSpaceOccupation> checkPermissionsToManageLessonInstanceSpaceOccupations =
            new AccessControlPredicate<LessonInstanceSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return true;
                }
            };
//
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

//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(loggedPerson);
                    return true;
                }
            };

}
