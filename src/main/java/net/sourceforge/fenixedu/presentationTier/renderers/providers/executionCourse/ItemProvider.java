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
import net.sourceforge.fenixedu.presentationTier.Action.manager.ItemCreator;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public abstract class ItemProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Item self;
        Section section;

        if (source instanceof Item) {
            self = (Item) source;
            section = self.getSection();;
        } else if (source instanceof ItemCreator) {
            ItemCreator creator = (ItemCreator) source;

            self = null;
            section = creator.getSection();
        } else {
            throw new RuntimeException("type not supported");
        }

        return provideForContext(section, self);
    }

    public abstract Object provideForContext(Section section, Item self);

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
