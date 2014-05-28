/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;

public class PersonNamePart extends PersonNamePart_Base {

    public PersonNamePart(final String namePart) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        return StringNormalizer.normalize(string.trim());
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

        for (final PersonNamePart personNamePart : Bennu.getInstance().getPersonNamePartSet()) {
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
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.PersonName> getPersonName() {
        return getPersonNameSet();
    }

    @Deprecated
    public boolean hasAnyPersonName() {
        return !getPersonNameSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNamePart() {
        return getNamePart() != null;
    }

}
