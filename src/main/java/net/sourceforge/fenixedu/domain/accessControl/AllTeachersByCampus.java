package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AllTeachersByCampus extends RoleByCampusGroup {

    public AllTeachersByCampus(Campus campus) {
        super(RoleType.TEACHER, campus);
    }

    @Override
    protected boolean isPersonInCampus(Person person, Campus campus) {
        return person.getTeacher().teachesAt(campus);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getCampus()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            final Campus campus = (Campus) arguments[0];
            if (campus == null) {
                throw new VariableNotDefinedException("campus");
            }
            return new AllTeachersByCampus(campus);

        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

    }

    @Override
    public PersistentTeacherGroup convert() {
        return PersistentTeacherGroup.getInstance(getCampus());
    }
}
