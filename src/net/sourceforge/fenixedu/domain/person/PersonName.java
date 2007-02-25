package net.sourceforge.fenixedu.domain.person;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PersonName extends PersonName_Base implements Comparable<PersonName> {

    public static class PersonNameLimitedOrderedSet extends TreeSet<PersonName> {

	private final int maxElements;

	public PersonNameLimitedOrderedSet(final int maxElements) {
	    super();
	    this.maxElements = maxElements;
	}

	@Override
	public boolean add(final PersonName personName) {
	    if (size() < maxElements) {
		return super.add(personName);
	    }
	    final PersonName lastPersonName = last();
	    if (lastPersonName.compareTo(personName) > 0) {
		remove(lastPersonName);
		return super.add(personName);
	    }
	    return false;
	}
    }

    public static class ExternalPersonNameLimitedOrderedSet extends PersonNameLimitedOrderedSet {

	public ExternalPersonNameLimitedOrderedSet(final int maxElements) {
	    super(maxElements);
	}

	@Override
	public boolean add(final PersonName personName) {
	    final Person person = personName.getPerson();
	    return person.hasExternalPerson() ? super.add(personName) : false;
	}
    }


    public  PersonName() {
        super();
    }

    public int compareTo(PersonName personName) {
	return getName().compareTo(personName.getName());
    }

    @Override
    public void setName(String name) {
	super.setName(PersonNamePart.normalize(name));
	PersonNamePart.reindex(this);
    }

    private static boolean conatinsAll(final String normalizedPersonName, final String[] nameParts) {
	for (final String namePart : nameParts) {
	    if (normalizedPersonName.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }

    public static void find(final PersonNameLimitedOrderedSet personNameLimitedOrderedSet, final String name, final int size) {
	final String[] nameParts = PersonNamePart.getNameParts(name);
	if (nameParts.length > 0) {
	    final PersonNamePart personNamePart = PersonNamePart.find(nameParts[0]);
	    if (personNamePart != null && nameParts.length == 1) {
		personNameLimitedOrderedSet.addAll(personNamePart.getPersonNameSet());
	    } else {
		final Set<PersonName> personNames = personNamePart == null ?
			RootDomainObject.getInstance().getPersonNameSet() : personNamePart.getPersonNameSet();
		for (final PersonName personName : personNames) {
		    final String normalizedPersonName = personName.getName();
		    if (conatinsAll(normalizedPersonName, nameParts)) {
			personNameLimitedOrderedSet.add(personName);
		    }
		}
	    }
	}	
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
	    if (person.hasExternalPerson()) {
		textToAppend = person.getExternalPerson().getInstitutionUnit().getName();
	    } else {
		textToAppend = person.getUsername();
	    }
	}
	appendTextBetweenCommas(text, textToAppend);

	return text.toString();
    }

    private void appendTextBetweenCommas(final StringBuilder stringBuilder, final String string) {
	stringBuilder.append(" (");
	stringBuilder.append(string);
	stringBuilder.append(")");
    }

}
