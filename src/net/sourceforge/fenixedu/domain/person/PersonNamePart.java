package net.sourceforge.fenixedu.domain.person;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class PersonNamePart extends PersonNamePart_Base {

    public PersonNamePart(final String namePart) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setNamePart(namePart);
    }

    @Override
    public void setNamePart(final String namePart) {
	if (namePart == null) {
	    throw new DomainException("error.name.part.cannot.be.null");
	}
	final PersonNamePart personNamePart = find(namePart);
	if (personNamePart != null && personNamePart != this) {
	    throw new DomainException("error.duplicate.name.part", namePart);
	}
	super.setNamePart(namePart);
    }

    public static String normalize(final String string) {
	return StringNormalizer.normalize(string.trim()).toLowerCase();
    }
    
    public static String[] getNameParts(final String name) {
	return normalize(name).split(" ");
    }

    private static final Map<String, PersonNamePart> personNamePartIndexMap = new HashMap<String, PersonNamePart>();

    public static PersonNamePart find(final String namePart) {
	final String normalizedNamePart = StringNormalizer.normalize(namePart);

	final PersonNamePart indexedPersonNamePart = personNamePartIndexMap.get(normalizedNamePart);
	if (indexedPersonNamePart != null) {
	    return indexedPersonNamePart;
	}

	for (final PersonNamePart personNamePart : RootDomainObject.getInstance().getPersonNamePartSet()) {
	    final String otherPersonNamePart = personNamePart.getNamePart();
	    if (!personNamePartIndexMap.containsKey(otherPersonNamePart)) {
		personNamePartIndexMap.put(otherPersonNamePart, personNamePart);
	    }
	    if (normalizedNamePart.equals(otherPersonNamePart)) {
		return personNamePart;
	    }
	}
	return null;
    }

    protected static PersonNamePart findAndCreateIfNotFound(final String namePart) {
	final PersonNamePart personNamePart = find(namePart);
	return personNamePart == null ? new PersonNamePart(namePart) : personNamePart;
    }

    protected static void index(final PersonName personName, final String namePart) {
	final PersonNamePart personNamePart = findAndCreateIfNotFound(namePart);
	personNamePart.addPersonName(personName);
    }

    protected static void index(final PersonName personName, final String[] nameParts) {
	for (final String namePart : nameParts) {
	    index(personName, namePart);
	}
    }

    protected static void index(final PersonName personName) {
	index(personName, getNameParts(personName.getName()));
    }

    public static void reindex(final PersonName personName) {
	personName.getPersonNamePartSet().clear();
	index(personName);
    }

    public void deleteIfEmpty() {
	if (getPersonNameSet().isEmpty()) {
	    removeRootDomainObject();
	    deleteDomainObject();
	}
    }

}
