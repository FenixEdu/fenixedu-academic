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

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

public class SectionOrderProvider extends SectionProvider {

    @Override
    public Object provideForContext(Site site, Section superiorSection, Section self) {
        Collection<Section> siblings;

        if (superiorSection != null) {
            siblings = superiorSection.getOrderedSubSections();
        } else {
            siblings = site.getOrderedAssociatedSections();
        }

        siblings = new ArrayList<Section>(siblings);
        siblings.remove(self);

        return siblings;
    }

}
