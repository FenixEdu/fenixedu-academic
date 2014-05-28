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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * This is a specific renderer used to create the side menu of a UnitSite. This
 * renderer is very similar to the standard Site renderer except that it uses
 * the subsections of a specific top level section.
 * 
 * <p>
 * <ul>
 * <li>Top
 * <ul>
 * <li>A
 * <li>B
 * </ul>
 * <li>C
 * </ul>
 * If the site contains a section structure like the one presented then only the subsections of top (A and B) are presented.
 * 
 * @author cfgi
 */
public class UnitSiteTopMenuRenderer extends UnitSiteMenuRenderer {

    @Override
    protected MultiLanguageString getTargetSectionName() {
        return i18n("Topo", "Top");
    }

    @Override
    protected List<TemplatedSection> getDefaultEntries(Site site) {
        return Collections.emptyList();
    }

    @Override
    protected boolean allowsSubMenus() {
        return false;
    }
}