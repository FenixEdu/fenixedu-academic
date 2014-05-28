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
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;

public class UnitNamePart extends UnitNamePart_Base {

    public UnitNamePart(final String namePart) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setNamePart(namePart);
    }

    @Override
    public void setNamePart(final String namePart) {
        if (namePart == null) {
            throw new DomainException("error.name.part.cannot.be.null");
        }
        final UnitNamePart unitNamePart = find(namePart);
        if (unitNamePart != null && unitNamePart != this) {
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

    private static final Map<String, UnitNamePart> unitNamePartIndexMap = new HashMap<String, UnitNamePart>();

    public static UnitNamePart find(final String namePart) {
        final String normalizedNamePart = StringNormalizer.normalize(namePart);

        final UnitNamePart indexedUnitNamePart = unitNamePartIndexMap.get(normalizedNamePart);
        if (indexedUnitNamePart != null) {
            return indexedUnitNamePart;
        }

        for (final UnitNamePart unitNamePart : Bennu.getInstance().getUnitNamePartSet()) {
            final String otherUnitNamePart = unitNamePart.getNamePart();
            if (!unitNamePartIndexMap.containsKey(otherUnitNamePart)) {
                unitNamePartIndexMap.put(otherUnitNamePart, unitNamePart);
            }
            if (normalizedNamePart.equals(otherUnitNamePart)) {
                return unitNamePart;
            }
        }
        return null;
    }

    protected static UnitNamePart findAndCreateIfNotFound(final String namePart) {
        final UnitNamePart unitNamePart = find(namePart);
        return unitNamePart == null ? new UnitNamePart(namePart) : unitNamePart;
    }

    protected static void index(final UnitName unitName, final String namePart) {
        final UnitNamePart unitNamePart = findAndCreateIfNotFound(namePart);
        unitNamePart.addUnitName(unitName);
    }

    protected static void index(final UnitName unitName, final String[] nameParts) {
        for (final String namePart : nameParts) {
            index(unitName, namePart);
        }
    }

    protected static void index(final UnitName unitName) {
        index(unitName, getNameParts(unitName.getName()));
    }

    public static void reindex(final UnitName unitName) {
        unitName.getUnitNamePartSet().clear();
        index(unitName);
    }

    public void deleteIfEmpty() {
        if (getUnitNameSet().isEmpty()) {
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitName> getUnitName() {
        return getUnitNameSet();
    }

    @Deprecated
    public boolean hasAnyUnitName() {
        return !getUnitNameSet().isEmpty();
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
