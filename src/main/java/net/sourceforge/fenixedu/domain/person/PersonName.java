package net.sourceforge.fenixedu.domain.person;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import com.google.common.base.Predicate;

public class PersonName extends PersonName_Base implements Comparable<PersonName> {

    public static class PersonNameLimitedOrderedSet extends TreeSet<PersonName> {

        private final int maxElements;
        private final Predicate<Person> predicate;

        public PersonNameLimitedOrderedSet(final int maxElements, final Predicate<Person> predicate) {
            super();
            this.maxElements = maxElements;
            this.predicate = predicate;
        }

        public PersonNameLimitedOrderedSet(final int maxElements) {
            this(maxElements, null);
        }

        @Override
        public boolean add(final PersonName personName) {
            if (predicate == null || (personName.getPerson() != null && predicate.apply(personName.getPerson()))) {
                if (size() < maxElements) {
                    return super.add(personName);
                }
                final PersonName lastPersonName = last();
                if (lastPersonName.compareTo(personName) > 0) {
                    remove(lastPersonName);
                    return super.add(personName);
                }
            }
            return false;
        }
    }

    public static class InternalPersonNameLimitedOrderedSet extends PersonNameLimitedOrderedSet {

        public InternalPersonNameLimitedOrderedSet(final int maxElements) {
            super(maxElements);
        }

        @Override
        public boolean add(final PersonName personName) {
            return personName.getIsExternalPerson() ? false : super.add(personName);
        }
    }

    public static class ExternalPersonNameLimitedOrderedSet extends PersonNameLimitedOrderedSet {

        public ExternalPersonNameLimitedOrderedSet(final int maxElements) {
            super(maxElements);
        }

        @Override
        public boolean add(final PersonName personName) {
            return personName.getIsExternalPerson() ? super.add(personName) : false;
        }
    }

    public PersonName(Person person) {
        super();
        this.setRootDomainObject(RootDomainObject.getInstance());
        setPerson(person);
        setIsExternalPerson(Boolean.valueOf(person.hasExternalContract()));
    }

    @Override
    public int compareTo(PersonName personName) {
        final int stringCompare = getName().compareTo(personName.getName());
        return stringCompare == 0 ? getExternalId().compareTo(personName.getExternalId()) : stringCompare;
    }

    @Override
    public void setName(String name) {
        super.setName(PersonNamePart.normalize(name));
        PersonNamePart.reindex(this);
    }

    private static boolean containsAll(final String normalizedPersonName, final String[] nameParts) {
        for (final String namePart : nameParts) {
            if (normalizedPersonName.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean match(String[] parts) {
        return containsAll(getName(), parts);
    }

    public static void find(final PersonNameLimitedOrderedSet personNameLimitedOrderedSet, final String name, final int size) {
        final String[] nameParts = PersonNamePart.getNameParts(name);
        if (nameParts.length > 0) {
            final PersonNamePart personNamePart = PersonNamePart.find(nameParts[0]);
            if (personNamePart != null && nameParts.length == 1) {
                personNameLimitedOrderedSet.addAll(personNamePart.getPersonNameSet());
            } else {
                final Set<PersonName> personNames =
                        personNamePart == null ? RootDomainObject.getInstance().getPersonNameSet() : personNamePart
                                .getPersonNameSet();
                for (final PersonName personName : personNames) {
                    final String normalizedPersonName = personName.getName();
                    if (containsAll(normalizedPersonName, nameParts)) {
                        personNameLimitedOrderedSet.add(personName);
                    }
                }
            }
        }
    }

    public static Collection<PersonName> findPerson(final String name, final int size, final Predicate<Person> predicate) {
        final PersonNameLimitedOrderedSet personNameLimitedOrderedSet = new PersonNameLimitedOrderedSet(size, predicate);
        find(personNameLimitedOrderedSet, name, size);
        return personNameLimitedOrderedSet;
    }

    public static Collection<PersonName> findPerson(final String name, final int size) {
        final PersonNameLimitedOrderedSet personNameLimitedOrderedSet = new PersonNameLimitedOrderedSet(size);
        find(personNameLimitedOrderedSet, name, size);
        return personNameLimitedOrderedSet;
    }

    public static Collection<PersonName> findInternalPerson(final String name, final int size) {
        final InternalPersonNameLimitedOrderedSet personNameLimitedOrderedSet = new InternalPersonNameLimitedOrderedSet(size);
        find(personNameLimitedOrderedSet, name, size);
        return personNameLimitedOrderedSet;
    }

    public static Collection<PersonName> findExternalPerson(final String name, final int size) {
        final ExternalPersonNameLimitedOrderedSet personNameLimitedOrderedSet = new ExternalPersonNameLimitedOrderedSet(size);
        find(personNameLimitedOrderedSet, name, size);
        return personNameLimitedOrderedSet;
    }

    public static Collection<PersonName> find(final String name, final int size) {
        final PersonNameLimitedOrderedSet personNameLimitedOrderedSet = new PersonNameLimitedOrderedSet(size);
        find(personNameLimitedOrderedSet, name, size);
        return personNameLimitedOrderedSet;
    }

    public String getText() {
        final Person person = getPerson();

        final StringBuilder text = new StringBuilder();
        text.append(person.getName());

        final String textToAppend;
        Employee employee = person.getEmployee();
        if (employee != null && employee.getLastWorkingPlace() != null) {
            textToAppend = employee.getLastWorkingPlace().getName();
        } else {
            if (person.hasExternalContract()) {
                textToAppend = person.getExternalContract().getInstitutionUnit().getName();
            } else {
                textToAppend = person.getMostImportantAlias();
            }
        }
        appendText(text, textToAppend);

        return text.toString();
    }

    private void appendText(final StringBuilder stringBuilder, final String string) {
        if (string != null) {
            stringBuilder.append(" (");
            stringBuilder.append(string);
            stringBuilder.append(")");
        }
    }

    public void delete() {
        final Set<PersonNamePart> personNameParts = new HashSet<PersonNamePart>(getPersonNamePartSet());
        getPersonNamePartSet().clear();
        removeRootDomainObject();
        removePerson();
        deleteDomainObject();
        for (final PersonNamePart personNamePart : personNameParts) {
            personNamePart.deleteIfEmpty();
        }
    }

}
