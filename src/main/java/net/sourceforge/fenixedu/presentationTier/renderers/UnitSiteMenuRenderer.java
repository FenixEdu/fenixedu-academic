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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class UnitSiteMenuRenderer extends SiteMenuRenderer {

    @Override
    protected Collection<? extends CmsContent> getInitialEntries(Site site) {
        List<CmsContent> contents = new ArrayList<>();
        contents.addAll(getDefaultEntries(site));
        Section targetSection = getTargetSection(site);
        if (targetSection != null) {
            contents.addAll(targetSection.getOrderedSubSections());
        }
        return contents;
    }

    protected List<TemplatedSection> getDefaultEntries(Site site) {
        return site.getTemplate().getOrderedSections();
    }

    protected Section getTargetSection(Site site) {
        MultiLanguageString name = getTargetSectionName();
        for (Section section : site.getAssociatedSectionSet()) {
            if (equalInAnyLanguage(section.getName(), name)) {
                return section;
            }
        }
        return null;
    }

    protected MultiLanguageString i18n(String pt, String en) {
        return new MultiLanguageString(MultiLanguageString.pt, pt).with(MultiLanguageString.en, en);
    }

    protected abstract MultiLanguageString getTargetSectionName();

    protected boolean equalInAnyLanguage(MultiLanguageString target, MultiLanguageString sub) {
        return target.equalInAnyLanguage(sub);
    }

}