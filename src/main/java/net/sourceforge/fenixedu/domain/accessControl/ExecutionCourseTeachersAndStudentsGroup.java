package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

import org.fenixedu.bennu.core.domain.User;

public class ExecutionCourseTeachersAndStudentsGroup extends ExecutionCourseGroup {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private final GroupUnion union;

    public ExecutionCourseTeachersAndStudentsGroup(ExecutionCourse executionCourse) {
        super(executionCourse);

        ExecutionCourseTeachersGroup teachers = new ExecutionCourseTeachersGroup(executionCourse);
        ExecutionCourseStudentsGroup students = new ExecutionCourseStudentsGroup(executionCourse);

        this.union = new GroupUnion(teachers, students);
    }

    @Override
    public String getPresentationNameKey() {
        return "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroupWithName";
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
    public boolean allows(User userView) {
        return this.union.allows(userView);
    }

    @Override
    public boolean isMember(Person person) {
        return this.union.isMember(person);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new ExecutionCourseTeachersAndStudentsGroup((ExecutionCourse) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return union.convert();
    }
}
