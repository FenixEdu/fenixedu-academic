package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class ExecutionCourseResponsibleTeachersGroup extends AbstractExecutionCourseTeachersGroup {

    private static final long serialVersionUID = 1L;

    public ExecutionCourseResponsibleTeachersGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }

    @Override
    public String getPresentationNameKey() {
        return "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName";
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new ExecutionCourseResponsibleTeachersGroup((ExecutionCourse) arguments[0]);
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
    public Collection<Professorship> getProfessorships() {
        return getExecutionCourse().responsibleFors();
    }

    @Override
    public PersistentTeacherResponsibleOfExecutionCourseGroup convert() {
        return PersistentTeacherResponsibleOfExecutionCourseGroup.getInstance(getExecutionCourse());
    }
}
