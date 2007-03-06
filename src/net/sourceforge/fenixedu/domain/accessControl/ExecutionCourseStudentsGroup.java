package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

import org.apache.commons.collections.Transformer;

public class ExecutionCourseStudentsGroup extends ExecutionCourseGroup {

    private static final long serialVersionUID = 1L;

    private class AttendPersonTransformer implements Transformer {
        public Object transform(Object object) {
            Attends attend = (Attends) object;
            return attend.getRegistration().getPerson();
        }
    }

    public ExecutionCourseStudentsGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }

    @Override
    public int getElementsCount() {
        return this.getExecutionCourse().getAttendsCount();
    }

    @Override
    public Set<Person> getElements() {
        return super.freezeSet(new HashSet<Person>(CollectionUtils.collect(getExecutionCourse()
                .getAttends(), new AttendPersonTransformer())));
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent() && hasExecutionCourse()) {
            for (final Attends attends : getExecutionCourse().getAttendsSet()) {
                if (attends.getRegistration().getStudent() == person.getStudent()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getObject())
        };
    }

    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            try {
                return new ExecutionCourseStudentsGroup((ExecutionCourse) arguments[0]);
            }
            catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse", arguments[0].toString());
            }
        }

        public int getMinArguments() {
            return 0;
        }

        public int getMaxArguments() {
            return 1;
        }
        
    }
}
