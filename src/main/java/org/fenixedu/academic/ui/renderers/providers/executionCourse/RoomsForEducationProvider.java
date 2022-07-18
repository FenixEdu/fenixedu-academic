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
package org.fenixedu.academic.ui.renderers.providers.executionCourse;

import java.text.Collator;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoomsForEducationProvider implements DataProvider {

    private final static Comparator<Space> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("externalId"));
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        Set<Space> rooms = new TreeSet<Space>(ROOM_COMPARATOR_BY_NAME);
        rooms.addAll(SpaceUtils.allocatableSpaces().filter(space -> space.isActive()).collect(Collectors.toList()));
        return rooms;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
