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

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SectionCreator;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public abstract class SectionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Section self;
        Section superiorSection;
        Site site;

        if (source instanceof Section) {
            self = (Section) source;

            superiorSection = self.getSuperiorSection();
            site = self.getOwnerSite();
        } else if (source instanceof SectionCreator) {
            SectionCreator creator = (SectionCreator) source;

            self = null;

            superiorSection = creator.getSuperiorSection();
            site = creator.getSite();
        } else {
            throw new RuntimeException("type not supported");
        }

        return provideForContext(site, superiorSection, self);
    }

    public abstract Object provideForContext(Site site, Section superiorSection, Section self);

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
