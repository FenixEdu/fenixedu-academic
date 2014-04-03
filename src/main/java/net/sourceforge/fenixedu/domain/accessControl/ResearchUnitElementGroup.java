package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchUnitElementGroup extends DomainBackedGroup<ResearchUnit> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ResearchUnitElementGroup(ResearchUnit unit) {
        super(unit);
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.ResearcherResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.researchUnitElement";
    }

    @Override
    public Set<Person> getElements() {
        return new HashSet<Person>(getObject().getPossibleGroupMembers());
    }

    @Override
    public boolean isMember(Person person) {
        return person != null && person.getWorkingResearchUnitsAndParents().contains(getObject());
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            ResearchUnit unit = (ResearchUnit) arguments[0];
            if (unit == null) {
                throw new VariableNotDefinedException("unit");
            }
            return new ResearchUnitElementGroup(unit);

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
        return PersistentUnitGroup.getInstance(getObject(), AccountabilityTypeEnum.RESEARCH_CONTRACT, null, false);
    }
}
