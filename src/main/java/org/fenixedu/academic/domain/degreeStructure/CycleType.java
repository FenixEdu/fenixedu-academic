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
package org.fenixedu.academic.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TreeSet;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public enum CycleType {

    FIRST_CYCLE(1, 180d),

    SECOND_CYCLE(2, FIRST_CYCLE, 120d),

    THIRD_CYCLE(3),

    SPECIALIZATION_CYCLE(4),

    SINGLE_CYCLE(5);

    static final public Comparator<CycleType> COMPARATOR_BY_LESS_WEIGHT = new Comparator<CycleType>() {
        @Override
        public int compare(CycleType o1, CycleType o2) {
            return o1.getWeight().compareTo(o2.getWeight());
        }
    };

    static final public Comparator<CycleType> COMPARATOR_BY_GREATER_WEIGHT = new Comparator<CycleType>() {
        @Override
        public int compare(CycleType o1, CycleType o2) {
            return -COMPARATOR_BY_LESS_WEIGHT.compare(o1, o2);
        }
    };

    private Integer weight;
    private CycleType sourceCycleAffinity;
    private Double credits;

    private CycleType(Integer weight) {
        this(weight, (CycleType) null);
    }

    private CycleType(final Integer weight, final CycleType sourceCycleAffinity) {
        this.weight = weight;
        this.sourceCycleAffinity = sourceCycleAffinity;
        this.credits = 0d;
    }

    private CycleType(Integer weight, Double credits) {
        this(weight, null, credits);
    }

    private CycleType(final Integer weight, final CycleType sourceCycleAffinity, Double credits) {
        this.weight = weight;
        this.sourceCycleAffinity = sourceCycleAffinity;
        this.credits = credits;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getQualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    public LocalizedString getDescriptionI18N() {
        return BundleUtil.getLocalizedString(Bundle.ENUMERATION, getQualifiedName());
    }

    public String getDescription() {
        return getDescriptionI18N().getContent();
    }

    public String getDescription(final Locale locale) {
        return getDescriptionI18N().getContent(locale);
    }

    public Double getEctsCredits() {
        return credits;
    }

    static final public Collection<CycleType> getSortedValues() {
        final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);
        result.addAll(Arrays.asList(values()));
        return result;
    }

    public boolean isBeforeOrEquals(final CycleType cycleType) {
        return COMPARATOR_BY_LESS_WEIGHT.compare(this, cycleType) <= 0;
    }

    public boolean hasSourceCycleAffinity() {
        return this.sourceCycleAffinity != null;
    }

    public CycleType getSourceCycleAffinity() {
        return this.sourceCycleAffinity;
    }

    public CycleType getNext() {
        final Iterator<CycleType> iterator = getSortedValues().iterator();

        for (CycleType cycleType = iterator.next(); iterator.hasNext(); cycleType = iterator.next()) {
            if (cycleType == this) {
                return iterator.next();
            }

            continue;
        }

        return null;
    }

    public boolean hasNext() {
        return getNext() != null;
    }

    public CycleType getPrevious() {
        final List<CycleType> sortedValues = new ArrayList<CycleType>(getSortedValues());
        final ListIterator<CycleType> listIterator = sortedValues.listIterator(sortedValues.size());

        for (CycleType cycleType = listIterator.previous(); listIterator.hasPrevious(); cycleType = listIterator.previous()) {
            if (cycleType == this) {
                return listIterator.previous();
            }

            continue;
        }

        return null;
    }

    public boolean hasPrevious() {
        return getPrevious() != null;
    }
}
