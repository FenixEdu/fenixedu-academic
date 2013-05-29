package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class FixedSetGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final List<Person> persons;

    protected FixedSetGroup() {
        super();

        this.persons = new ArrayList<Person>();
    }

    @Override
    public String getName() {
        Iterator<Person> iter = persons.iterator();
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

    public FixedSetGroup(Person... persons) {
        this();

        for (Person person : persons) {
            this.persons.add(person);
        }
    }

    public FixedSetGroup(Collection<Person> persons) {
        this();

        for (Person person : persons) {
            this.persons.add(person);
        }
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        for (Person reference : this.persons) {
            Person person = reference;

            if (person != null) {
                elements.add(person);
            }
        }

        return super.freezeSet(elements);
    }

    @Override
    public int getElementsCount() {
        return this.persons.size();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof FixedSetGroup)) {
            return false;
        }

        return this.persons.equals(((FixedSetGroup) object).persons);
    }

    @Override
    public int hashCode() {
        return this.persons.hashCode();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        Collection<Person> persons = getElements();
        Argument[] arguments = new Argument[persons.size()];

        int index = 0;
        for (Person person : persons) {
            arguments[index++] = new StaticArgument(person.getExternalId());
        }

        return arguments;
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            List<Person> persons = new ArrayList<Person>();

            for (Object object : arguments) {
                Person person;
                if (object instanceof String) {
                    person = AbstractDomainObject.fromExternalId((String) object);
                } else if (object instanceof Person) {
                    person = (Person) object;
                } else {
                    throw new WrongTypeOfArgumentException(1, String.class, object.getClass());
                }

                if (person != null) {
                    persons.add(person);
                }
            }

            return new FixedSetGroup(persons);
        }

        @Override
        public int getMinArguments() {
            return 0;
        }

        @Override
        public int getMaxArguments() {
            return Integer.MAX_VALUE;
        }

    }
}
