package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class GroupingGroup extends DomainBackedGroup<Grouping> {

    private static final long serialVersionUID = 1L;

    public GroupingGroup(Grouping grouping) {
        super(grouping);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> persons = super.buildSet();
        if (getObject() != null) {
            for (Attends attend : getObject().getAttends()) {
                persons.add(attend.getRegistration().getStudent().getPerson());
            }
        }
        return super.freezeSet(persons);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new GroupingGroup((Grouping) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.grouping.notGrouping",
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
    public PersistentGroupingGroup convert() {
        return PersistentGroupingGroup.getInstance(getObject());
    }
}
