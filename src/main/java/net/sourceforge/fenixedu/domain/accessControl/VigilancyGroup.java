package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

public class VigilancyGroup extends DomainBackedGroup<Vigilancy> {

    public VigilancyGroup(Vigilancy vigilancy) {
        super(vigilancy);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Set<Person> getElements() {
        Set<Person> persons = new HashSet<Person>();
        Vigilancy vigilancy = getObject();
        persons.addAll(vigilancy.getTeachers());
        persons.add(vigilancy.getVigilantWrapper().getPerson());
        return persons;
    }

    @Override
    public String getName() {
        Iterator<Person> iter = getElements().iterator();
        Person person;
        String name = new String();
        while (iter.hasNext()) {
            person = iter.next();
            name += person.getName();
            if (iter.hasNext()) {
                name += "\n";
            }
        }
        return name;
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            Vigilancy vigilancy = (Vigilancy) arguments[0];
            if (vigilancy == null) {
                throw new VariableNotDefinedException("vigilancy");
            }
            return new VigilancyGroup(vigilancy);

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
    public PersistentVigilancyGroup convert() {
        return PersistentVigilancyGroup.getInstance(getObject());
    }
}
