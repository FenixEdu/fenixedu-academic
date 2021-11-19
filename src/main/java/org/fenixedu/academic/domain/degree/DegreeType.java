/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package org.fenixedu.academic.domain.degree;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.CycleTypes;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

/**
 * 
 * @author Nuno Nunes &amp; Joana Mota
 */
public class DegreeType extends DegreeType_Base implements Comparable<DegreeType> {

    public DegreeType(LocalizedString name) {
        setBennu(Bennu.getInstance());
        setName(name);
        setCycles(new CycleTypes());
        setCyclesToEnrol(new CycleTypes());
    }

    public final Collection<CycleType> getCycleTypes() {
        return getCycles().getTypes();
    }

    public final void setCycleTypes(Collection<CycleType> types) {
        setCycles(new CycleTypes(types));
    }

    public Collection<CycleType> getSupportedCyclesToEnrol() {
        return getCyclesToEnrol().getTypes();
    }

    public final void setCycleTypesToEnrol(Collection<CycleType> types) {
        setCyclesToEnrol(new CycleTypes(types));
    }

    public boolean isEmpty() {
        return getEmpty();
    }

    public boolean isSpecializationDegree() {
        return getCycleTypes().contains(CycleType.SPECIALIZATION_CYCLE);
    }

    public boolean isAdvancedSpecializationDiploma() {
        return getDea();
    }

    public boolean isAdvancedFormationDiploma() {
        return getDfa();
    }

    public boolean isPreBolonhaDegree() {
        return !this.isBolonhaType() && !this.getMinor() && !this.getUnstructured() && this.isDegree();
    }

    public boolean isBolonhaDegree() {
        return this.isBolonhaType() && this.isDegree();
    }

    public boolean isPreBolonhaMasterDegree() {
        return !this.isBolonhaType() && this.isMasterDegree();
    }

    public boolean isBolonhaMasterDegree() {
        return this.isBolonhaType() && this.isMasterDegree();
    }

    public boolean isBolonhaType() {
        return getBolonha();
    }

    public boolean isDegree() {
        return getDegreeType();
    }

    public boolean isMasterDegree() {
        return getMasterDegree();
    }

    public boolean isIntegratedMasterDegree() {
        return getCycleTypes().contains(CycleType.FIRST_CYCLE) && getCycleTypes().contains(CycleType.SECOND_CYCLE);
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return this.isDegree() || isIntegratedMasterDegree();
    }

    final public String getCreditsDescription() {
        return this.isPreBolonhaMasterDegree() ? " Créd." : " ECTS";
    }

    public String getPrefix() {
        return getPrefix(I18N.getLocale());
    }

    public String getPrefix(final Locale locale) {
        final StringBuilder result = new StringBuilder();
        if (this.isAdvancedSpecializationDiploma()) {
            return result.toString();
        }
        if (this.isAdvancedFormationDiploma()) {
            result.append(BundleUtil.getString(Bundle.ACADEMIC, locale, "degree.DegreeType.prefix.one")).append(" ");
            return result.toString();
        }
        result.append(BundleUtil.getString(Bundle.ACADEMIC, locale, "degree.DegreeType.prefix.two"));
        return result.toString();
    }

    public boolean isFirstCycle() {
        return getCycleTypes().contains(CycleType.FIRST_CYCLE);
    }

    public boolean isSecondCycle() {
        return getCycleTypes().contains(CycleType.SECOND_CYCLE);
    }

    public boolean isThirdCycle() {
        return getCycleTypes().contains(CycleType.THIRD_CYCLE);
    }

    public boolean isSpecializationCycle() {
        return getCycleTypes().contains(CycleType.SPECIALIZATION_CYCLE);
    }

    final public boolean hasAnyCycleTypes() {
        return !getCycleTypes().isEmpty();
    }

    final public boolean hasCycleTypes(final CycleType cycleType) {
        return getCycleTypes().contains(cycleType);
    }

    final public boolean isComposite() {
        return getCycleTypes().size() > 1;
    }

    final public boolean hasExactlyOneCycleType() {
        return getCycleTypes().size() == 1;
    }

    final public CycleType getCycleType() {
        if (hasExactlyOneCycleType()) {
            return getCycleTypes().iterator().next();
        }

        throw new DomainException("DegreeType.has.more.than.one.cycle.type");
    }

    final public boolean isStrictlyFirstCycle() {
        return hasExactlyOneCycleType() && getCycleTypes().contains(CycleType.FIRST_CYCLE);
    }

    public CycleType getFirstOrderedCycleType() {
        final TreeSet<CycleType> ordered = getOrderedCycleTypes();
        return ordered.isEmpty() ? null : ordered.first();
    }

    public CycleType getLastOrderedCycleType() {
        final TreeSet<CycleType> ordered = getOrderedCycleTypes();
        return ordered.isEmpty() ? null : ordered.last();
    }

    public TreeSet<CycleType> getOrderedCycleTypes() {
        TreeSet<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);
        result.addAll(getCycleTypes());
        return result;
    }

    public static Stream<DegreeType> all() {
        return Bennu.getInstance().getDegreeTypeSet().stream();
    }

    public static Optional<DegreeType> matching(Predicate<DegreeType> predicate) {
        return all().filter(predicate).findAny();
    }

    @Override
    public int compareTo(DegreeType o) {
        return getName().compareTo(o.getName());
    }

    private static final Predicate<DegreeType> nonNull = type -> type != null;

    @SafeVarargs
    public static Predicate<DegreeType> oneOf(Predicate<DegreeType> one, Predicate<DegreeType>... others) {
        for (Predicate<DegreeType> pred : others) {
            one = one.or(pred);
        }
        return nonNull.and(one);
    }

}
