package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

/**
 * This group is an abstraction of all person currently performing a {@link Function}. The elements of the group are all person
 * that have and
 * active {@link PersonFunction} connected to the function.
 * 
 * @author cfgi
 */
public class PersonsInFunctionGroup extends DomainBackedGroup<Function> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public PersonsInFunctionGroup(Function object) {
        super(object);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> set = buildSet();

        YearMonthDay today = new YearMonthDay();
        for (PersonFunction function : getObject().getPersonFunctions()) {
            if (function.isActive(today)) {
                set.add(function.getPerson());
            }
        }

        return set;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getObject().getName(), getObject().getUnit().getName() };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new PersonsInFunctionGroup((Function) arguments[0]);
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
    public PersistentPersonsInFunctionGroup convert() {
        return PersistentPersonsInFunctionGroup.getInstance(getObject());
    }
}
