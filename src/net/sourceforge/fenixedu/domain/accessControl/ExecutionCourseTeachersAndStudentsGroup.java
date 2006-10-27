package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;

public class ExecutionCourseTeachersAndStudentsGroup extends ExecutionCourseGroup {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private GroupUnion union;

    public ExecutionCourseTeachersAndStudentsGroup(ExecutionCourse executionCourse) {
        super(executionCourse);

        ExecutionCourseTeachersGroup teachers = new ExecutionCourseTeachersGroup(executionCourse);
        ExecutionCourseStudentsGroup students = new ExecutionCourseStudentsGroup(executionCourse);
        
        this.union = new GroupUnion(teachers, students);
    }

    @Override
    public Set<Person> getElements() {
        return this.union.getElements();
    }

    @Override
    public int getElementsCount() {
        return this.union.getElementsCount();
    }

    @Override
    public boolean allows(IUserView userView) {
        return this.union.allows(userView);
    }

    @Override
    public boolean isMember(Person person) {
        return this.union.isMember(person);
    }
    
}
