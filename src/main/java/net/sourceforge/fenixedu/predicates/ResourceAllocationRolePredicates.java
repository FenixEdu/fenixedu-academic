package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationStateInstant;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResourceAllocationRolePredicates {

    // Lesson Predicates

    public static final AccessControlPredicate<Lesson> checkPermissionsToManageLessons = new AccessControlPredicate<Lesson>() {
        @Override
        public boolean evaluate(Lesson lesson) {
            return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
        }
    };

    // Shift Predicates

    public static final AccessControlPredicate<Shift> checkPermissionsToManageShifts = new AccessControlPredicate<Shift>() {
        @Override
        public boolean evaluate(Shift shift) {
            return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
        }
    };

    // SchoolClass Predicates

    public static final AccessControlPredicate<SchoolClass> checkPermissionsToManageSchoolClass =
            new AccessControlPredicate<SchoolClass>() {
                @Override
                public boolean evaluate(SchoolClass schoolClass) {
                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    // Lesson Instance Predicates

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstances =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {
                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstancesWithTeacherCheck =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                        return true;
                    }

                    ExecutionCourse executionCourse = lessonInstance.getLesson().getExecutionCourse();
                    if (loggedPerson.getProfessorships().size() > 0
                            && loggedPerson.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }

                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    // Punctual Rooms Occupation Management Predicates

    public static final AccessControlPredicate<PunctualRoomsOccupationRequest> checkPermissionsToManagePunctualRoomsOccupationRequests =
            new AccessControlPredicate<PunctualRoomsOccupationRequest>() {
                @Override
                public boolean evaluate(PunctualRoomsOccupationRequest request) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.hasRole(RoleType.TEACHER) || loggedPerson.hasAnyProfessorships()) {
                        return true;
                    }

                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    public static final AccessControlPredicate<PunctualRoomsOccupationComment> checkPermissionsToManagePunctualRoomsOccupationComments =
            new AccessControlPredicate<PunctualRoomsOccupationComment>() {
                @Override
                public boolean evaluate(PunctualRoomsOccupationComment comment) {
                    return checkPermissionsToManagePunctualRoomsOccupationRequests.evaluate(comment.getRequest());
                }
            };

    public static final AccessControlPredicate<PunctualRoomsOccupationStateInstant> checkPermissionsToManagePunctualRoomsOccupationStateInstants =
            new AccessControlPredicate<PunctualRoomsOccupationStateInstant>() {
                @Override
                public boolean evaluate(PunctualRoomsOccupationStateInstant stateInstant) {
                    return checkPermissionsToManagePunctualRoomsOccupationRequests.evaluate(stateInstant.getRequest());
                }
            };

}
