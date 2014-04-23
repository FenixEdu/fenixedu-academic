package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersistentUnitGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

public class DepartmentPresidentGroup extends DomainBackedGroup<DepartmentUnit> {

    private static final long serialVersionUID = 1L;

    public DepartmentPresidentGroup(final DepartmentUnit unit) {
        super(unit);
    }

    @Override
    public Set<Person> getElements() {
        final DepartmentUnit unit = getObject();
        final Person person = unit.getCurrentDepartmentPresident();
        return person == null ? Collections.EMPTY_SET : Collections.singleton(person);
    }

    @Override
    public boolean isMember(final Person person) {
        final DepartmentUnit unit = getObject();
        return person != null && person == unit.getCurrentDepartmentPresident();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new DepartmentPresidentGroup((DepartmentUnit) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.unitMembers.notUnit",
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
    public PersistentUnitGroup convert() {
        return PersistentUnitGroup.getInstance(getObject(), null, FunctionType.PRESIDENT, false);
    }
}
