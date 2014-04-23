package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;

public class ScientificAreaMemberGroup extends DomainBackedGroup<ScientificAreaUnit> {

    public ScientificAreaMemberGroup(ScientificAreaUnit unit) {
        super(unit);
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.DepartmentMemberResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.scientificUnitElement";
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getObject().getName() };
    }

    @Override
    public boolean isMember(Person person) {
        return person != null && getObject().isUserMemberOfScientificArea(person);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> people = new HashSet<Person>();
        for (Contract contract : getObject().getWorkingContracts()) {
            people.add(contract.getPerson());
        }
        return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            ScientificAreaUnit unit = (ScientificAreaUnit) arguments[0];
            if (unit == null) {
                throw new VariableNotDefinedException("unit");
            }
            return new ScientificAreaMemberGroup(unit);

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
    public PersistentUnitGroup convert() {
        return PersistentUnitGroup.getInstance(getObject(), AccountabilityTypeEnum.WORKING_CONTRACT, null, false);
    }
}
