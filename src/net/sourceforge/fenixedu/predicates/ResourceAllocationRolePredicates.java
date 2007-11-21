package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationStateInstant;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResourceAllocationRolePredicates {

    // Lesson Predicates
    
    public static final AccessControlPredicate<Lesson> checkPermissionsToManageLessons = new AccessControlPredicate<Lesson>() {
	public boolean evaluate(Lesson lesson) {
	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());	    
	    return true;
	}
    };       

    // Shift Predicates
    
    public static final AccessControlPredicate<Shift> checkPermissionsToManageShifts = new AccessControlPredicate<Shift>() {
	public boolean evaluate(Shift shift) {
	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());	    
	    return true;
	}
    };
    
    // SchoolClass Predicates
    
    public static final AccessControlPredicate<SchoolClass> checkPermissionsToManageSchoolClass = new AccessControlPredicate<SchoolClass>() {
	public boolean evaluate(SchoolClass schoolClass) {
	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());	    
	    return true;
	}
    };
    
    // Lesson Instance Predicates
    
    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstances = new AccessControlPredicate<LessonInstance>() {
	public boolean evaluate(LessonInstance lessonInstance) {
	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());	    
	    return true;
	}
    };
    
    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstancesWithTeacherCheck = new AccessControlPredicate<LessonInstance>() {
	public boolean evaluate(LessonInstance lessonInstance) {
	    
	    Person loggedPerson = AccessControl.getPerson();
	    
	    if(loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
		return true;
	    }
	    
	    ExecutionCourse executionCourse = lessonInstance.getLesson().getExecutionCourse();	    	   
	    if(loggedPerson.hasTeacher() && loggedPerson.hasRole(RoleType.TEACHER) 
		    && loggedPerson.getTeacher().hasProfessorshipForExecutionCourse(executionCourse)) {
		return true;
	    }	
	    
	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(loggedPerson);	    
	    return true;
	}
    };      
    
    
    // Punctual Rooms Occupation Management Predicates
    
    public static final AccessControlPredicate<PunctualRoomsOccupationComment> checkPermissionsToManagePunctualRoomsOccupationComment = new AccessControlPredicate<PunctualRoomsOccupationComment>() {
	public boolean evaluate(PunctualRoomsOccupationComment comment) {
	    
	    Person loggedPerson = AccessControl.getPerson();
	    
	    if(loggedPerson.hasRole(RoleType.TEACHER)) {
		return true;
	    }

	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSpacesAllocation(loggedPerson);	    
	    return true;
	}
    };
    
    public static final AccessControlPredicate<PunctualRoomsOccupationStateInstant> checkPermissionsToManagePunctualRoomsOccupationStateInstant = new AccessControlPredicate<PunctualRoomsOccupationStateInstant>() {
	public boolean evaluate(PunctualRoomsOccupationStateInstant instant) {
	    
	    Person loggedPerson = AccessControl.getPerson();
	    
	    if(loggedPerson.hasRole(RoleType.TEACHER)) {
		return true;
	    }

	    ResourceAllocationRole.checkIfPersonHasPermissionToManageSpacesAllocation(loggedPerson);	    
	    return true;
	}
    };    
    
    // Resource Allocation Access Groups Predicates
    
    public static final AccessControlPredicate<ResourceAllocationRole> checkPermissionsToManageAccessGroups = new AccessControlPredicate<ResourceAllocationRole>() {
	public boolean evaluate(ResourceAllocationRole role) {
	    ResourceAllocationRole.checkIfPersonIsResourceAllocationSuperUser(AccessControl.getPerson());
	    return true;
	}
    };
}
