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
package net.sourceforge.fenixedu.domain.cms;

import java.util.List;

import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

public class SiteTemplate extends SiteTemplate_Base {

    public SiteTemplate(SiteTemplateController controller) {
        super();
        setController(controller);
        setBennu(Bennu.getInstance());
    }

    public static SiteTemplate getTemplateForSite(Site site) {
        for (SiteTemplate template : Bennu.getInstance().getSiteTemplateSet()) {
            if (template.getController().getControlledClass().equals(site.getClass())) {
                return template;
            }
        }
        return null;
    }

    public List<TemplatedSection> getOrderedSections() {
        return FluentIterable.from(getTemplatedSectionSet()).filter(new Predicate<TemplatedSection>() {
            @Override
            public boolean apply(TemplatedSection input) {
                return input.getVisible();
            }
        }).toSortedList(Ordering.natural());
    }

}
