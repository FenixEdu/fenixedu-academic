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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public class UnitName extends UnitName_Base implements Comparable<UnitName> {

    public static class UnitNameLimitedOrderedSet extends TreeSet<UnitName> {

        protected final int maxElements;

        public UnitNameLimitedOrderedSet(final int maxElements) {
            super();
            this.maxElements = maxElements;
        }

        @Override
        public boolean add(final UnitName unitName) {
            if (size() < maxElements) {
                return super.add(unitName);
            }
            final UnitName lastUnitName = last();
            if (lastUnitName.compareTo(unitName) > 0) {
                remove(lastUnitName);
                return super.add(unitName);
            }
            return false;
        }

        public boolean containsExactSameName(final UnitName unitName) {
            for (UnitName forUnitName : this) {
                if (forUnitName.getUnit().getName().equals(unitName.getUnit().getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class InternalUnitNameAndTypeLimitedOrderedSet extends UnitNameLimitedOrderedSet {

        private final Class<? extends Unit> unitType;

        public InternalUnitNameAndTypeLimitedOrderedSet(int maxElements, Class<? extends Unit> unitType) {
            super(maxElements);
            this.unitType = unitType;
        }

        @Override
        public boolean add(UnitName unitName) {
            if (size() < maxElements && unitName.getUnit().getClass().equals(unitType)) {
                return super.add(unitName);
            }
            final UnitName lastUnitName = isEmpty() ? null : last();
            if (lastUnitName != null && lastUnitName.compareTo(unitName) > 0 && unitName.getUnit().getClass().equals(unitType)) {
                remove(lastUnitName);
                return super.add(unitName);
            }
            return false;
        }
    }

    public static class InternalUnitNameLimitedOrderedSet extends UnitNameLimitedOrderedSet {

        public InternalUnitNameLimitedOrderedSet(final int maxElements) {
            super(maxElements);
        }

        @Override
        public boolean add(final UnitName unitName) {
            return unitName.getIsExternalUnit() ? false : super.add(unitName);
        }
    }

    public static class ExternalUnitNameLimitedOrderedSet extends UnitNameLimitedOrderedSet {

        private final Predicate predicate;

        public ExternalUnitNameLimitedOrderedSet(final int maxElements) {
            this(maxElements, null);
        }

        public ExternalUnitNameLimitedOrderedSet(final int maxElements, final Predicate predicate) {
            super(maxElements);
            this.predicate = predicate;
        }

        @Override
        public boolean add(final UnitName unitName) {
            return (predicate == null || predicate.evaluate(unitName)) && unitName.getIsExternalUnit() ? super.add(unitName) : false;
        }
    }

    public static class ExternalAcademicUnitNameLimitedOrderedSet extends UnitNameLimitedOrderedSet {

        private final Unit institutionUnit = Bennu.getInstance().getInstitutionUnit();

        public ExternalAcademicUnitNameLimitedOrderedSet(final int maxElements) {
            super(maxElements);
        }

        @Override
        public boolean add(final UnitName unitName) {
            String code = unitName.getUnit().getCode();
            return unitName.getIsExternalUnit() && !StringUtils.isEmpty(code) && StringUtils.isNumeric(code)
                    && super.add(unitName);
        }
    }

    public UnitName(Unit unit) {
        super();
        this.setRootDomainObject(Bennu.getInstance());
        setUnit(unit);
        setIsExternalUnit(Boolean.valueOf(!unit.isInternal()));
    }

    @Override
    public int compareTo(UnitName unitName) {
        final int stringCompare = getName().compareTo(unitName.getName());
        return stringCompare == 0 ? getExternalId().compareTo(unitName.getExternalId()) : stringCompare;
    }

    @Override
    public void setName(String name) {
        super.setName(UnitNamePart.normalize(name));
        UnitNamePart.reindex(this);
    }

    private static boolean containsAll(final String normalizedUnitName, final String[] nameParts) {
        for (final String namePart : nameParts) {
            if (normalizedUnitName.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    public static void find(final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet, final String name, final int size) {
        final String[] nameParts = UnitNamePart.getNameParts(name);
        if (nameParts.length > 0) {
            final UnitNamePart unitNamePart = UnitNamePart.find(nameParts[0]);
            if (unitNamePart != null && nameParts.length == 1) {
                unitNameLimitedOrderedSet.addAll(unitNamePart.getUnitNameSet());
            } else {
                final Set<UnitName> unitNames =
                        unitNamePart == null ? Bennu.getInstance().getUnitNameSet() : unitNamePart.getUnitNameSet();
                for (final UnitName unitName : unitNames) {
                    final String normalizedUnitName = unitName.getName();
                    if (containsAll(normalizedUnitName, nameParts)) {
                        unitNameLimitedOrderedSet.add(unitName);
                    }
                }
            }
        }
    }

    public static void findExternalInstitution(final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet, final String name,
            final int size, final UnitNameLimitedOrderedSet resultSet) {
        find(unitNameLimitedOrderedSet, name, size);
        Set<UnitName> restOfTheUnitNames = new HashSet<UnitName>();
        Set<UnitName> unitNamesWithScore = new HashSet<UnitName>();
        String[] nameParts = UnitNamePart.getNameParts(name);
        //adding first the units with more "score"
        for (UnitName unitName : unitNameLimitedOrderedSet) {
            if (unitName.getUnit().getCode() != null) {
                if (containsAllExactWords(unitName.getName(), nameParts)) {
                    if (!resultSet.containsExactSameName(unitName)) {
                        resultSet.add(unitName);
                        if (resultSet.size() == size) {
                            return;
                        }
                    }
                } else {
                    unitNamesWithScore.add(unitName);
                }
            } else {
                restOfTheUnitNames.add(unitName);
            }
        }
        //adding the unitNames with some score
        addDifferentUnitNames(size, resultSet, unitNamesWithScore);
        //adding the rest of the units until size
        addDifferentUnitNames(size, resultSet, restOfTheUnitNames);
    }

    private static void addDifferentUnitNames(final int size, final UnitNameLimitedOrderedSet resultSet,
            Set<UnitName> unitNamesWithScore) {
        for (UnitName unitName : unitNamesWithScore) {
            if (unitName.getUnit().getCode() == null) {
                if (!resultSet.containsExactSameName(unitName)) {
                    resultSet.add(unitName);
                    if (resultSet.size() == size) {
                        return;
                    }
                }
            }
        }
    }

    private static boolean containsAllExactWords(final String normalizedUnitName, final String[] nameParts) {
        final String[] unitNameParts = UnitNamePart.getNameParts(normalizedUnitName);
        for (final String namePart : nameParts) {
            if (namePart.length() > 3) {
                if (!existsCompleteNamePart(unitNameParts, namePart)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean existsCompleteNamePart(final String[] unitNameParts, final String namePart) {
        for (String unitPart : unitNameParts) {
            if (unitPart.equalsIgnoreCase(namePart)) {
                return true;
            }
        }
        return false;
    }

    public static void findExactWords(final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet, final String name) {
        final String[] nameParts = UnitNamePart.getNameParts(name);
        if (nameParts.length > 0) {
            final UnitNamePart unitNamePart = UnitNamePart.find(nameParts[0]);
            if (unitNamePart != null && nameParts.length == 1) {
                unitNameLimitedOrderedSet.addAll(unitNamePart.getUnitNameSet());
            } else {
                final Set<UnitName> unitNames =
                        unitNamePart == null ? Bennu.getInstance().getUnitNameSet() : unitNamePart.getUnitNameSet();
                for (final UnitName unitName : unitNames) {
                    final String normalizedUnitName = unitName.getName();
                    if (containsAllExactWords(normalizedUnitName, nameParts)) {
                        if (!existsTheSameCode(unitName, unitNameLimitedOrderedSet)) {
                            unitNameLimitedOrderedSet.add(unitName);
                        }
                    }
                }
            }
        }
    }

    private static boolean existsTheSameCode(UnitName unitName, UnitNameLimitedOrderedSet unitNameLimitedOrderedSet) {
        for (UnitName unitNameTemp : unitNameLimitedOrderedSet) {
            if (StringUtils.isEmpty(unitName.getUnit().getCode()) || !StringUtils.isNumeric(unitName.getUnit().getCode())) {
                return false;
            }
            if (unitName.getUnit().getCode().equals(unitNameTemp.getUnit().getCode())) {
                return true;
            }
        }
        return false;
    }

    public static Collection<UnitName> findInternalUnitWithType(final String name, final int size, Class<? extends Unit> unitType) {
        InternalUnitNameAndTypeLimitedOrderedSet internalUnitNameAndTypeLimitedOrderedSet =
                new InternalUnitNameAndTypeLimitedOrderedSet(size, unitType);
        find(internalUnitNameAndTypeLimitedOrderedSet, name, size);
        return internalUnitNameAndTypeLimitedOrderedSet;
    }

    public static Collection<UnitName> findInternalUnit(final String name, final int size) {
        final InternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new InternalUnitNameLimitedOrderedSet(size);
        find(unitNameLimitedOrderedSet, name, size);
        return unitNameLimitedOrderedSet;
    }

    public static Collection<UnitName> findExternalUnit(final String name, final int size) {
        final ExternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new ExternalUnitNameLimitedOrderedSet(size);
        find(unitNameLimitedOrderedSet, name, size);
        return unitNameLimitedOrderedSet;
    }

    public static Collection<UnitName> findExternalUnit(final String name, final int size, final Predicate predicate) {
        final ExternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet =
                new ExternalUnitNameLimitedOrderedSet(size, predicate);
        find(unitNameLimitedOrderedSet, name, size);
        return unitNameLimitedOrderedSet;
    }

    public static Collection<UnitName> findExternalAcademicUnit(final String name, final int size) {
        final ExternalAcademicUnitNameLimitedOrderedSet academicUnitNameLimitedOrderedSet =
                new ExternalAcademicUnitNameLimitedOrderedSet(size);
        findExactWords(academicUnitNameLimitedOrderedSet, name);
        return academicUnitNameLimitedOrderedSet;
    }

    /**
     * It does a broader search than the specified size, it then chooses the units with the code field filled first and
     * also exact matches with the complete normalized words then completes the list with the rest of the matches until the size
     * given
     * not adding unitNames with the name of the unit if that exact name it's not on the list
     * 
     * @param name
     * @param size
     * @return
     */
    public static Collection<UnitName> findExternalInstitutionUnitWithScore(final String name, final int size) {
        final ExternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new ExternalUnitNameLimitedOrderedSet(size + 400);
        final ExternalUnitNameLimitedOrderedSet resultSet = new ExternalUnitNameLimitedOrderedSet(size);
        findExternalInstitution(unitNameLimitedOrderedSet, name, size, resultSet);
        return resultSet;
    }

    public static Collection<UnitName> find(final String name, final int size) {
        final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new UnitNameLimitedOrderedSet(size);
        find(unitNameLimitedOrderedSet, name, size);
        return unitNameLimitedOrderedSet;
    }

    public void delete() {
        final Set<UnitNamePart> unitNameParts = new HashSet<UnitNamePart>(getUnitNamePartSet());
        getUnitNamePartSet().clear();
        setUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
        for (final UnitNamePart unitNamePart : unitNameParts) {
            unitNamePart.deleteIfEmpty();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart> getUnitNamePart() {
        return getUnitNamePartSet();
    }

    @Deprecated
    public boolean hasAnyUnitNamePart() {
        return !getUnitNamePartSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIsExternalUnit() {
        return getIsExternalUnit() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
