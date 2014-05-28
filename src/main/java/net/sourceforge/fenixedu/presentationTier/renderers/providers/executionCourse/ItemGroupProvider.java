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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;

import org.apache.commons.collections.list.UnmodifiableList;

import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ItemGroupProvider extends ItemProvider {

    @Override
    public Object provideForContext(Section section, Item self) {
        return UnmodifiableList.decorate(section.getOwnerSite().getContextualPermissionGroups());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
