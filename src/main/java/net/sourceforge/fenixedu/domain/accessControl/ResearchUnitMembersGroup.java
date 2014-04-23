package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.ClassOperator;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchUnitMembersGroup extends DomainBackedGroup<ResearchUnit> {

    private final Class classType;

    private static final long serialVersionUID = 1L;

    public ResearchUnitMembersGroup(ResearchUnit unit, Class type) {
        super(unit);
        this.classType = type;
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.MessagingResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label." + classType.getSimpleName();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> people = new HashSet<Person>();
        for (Accountability accountability : getObject().getActiveResearchContracts(classType)) {
            ResearchContract contract = (ResearchContract) accountability;
            people.add(contract.getPerson());
        }
        return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()), new ClassOperator(this.classType) };

    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && ((ResearchUnitMembersGroup) other).classType.equals(this.classType);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.classType.hashCode();
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            ResearchUnit unit = (ResearchUnit) arguments[0];
            Class type = (Class) arguments[1];
            if (unit == null || type == null) {
                throw new VariableNotDefinedException("unit or contract class");
            }
            return new ResearchUnitMembersGroup(unit, type);

        }

        @Override
        public int getMaxArguments() {
            return 2;
        }

        @Override
        public int getMinArguments() {
            return 2;
        }

    }

    @Override
    public PersistentUnitGroup convert() {
        return PersistentUnitGroup.getInstance(getObject(), AccountabilityTypeEnum.RESEARCH_CONTRACT, null, false);
    }
}
