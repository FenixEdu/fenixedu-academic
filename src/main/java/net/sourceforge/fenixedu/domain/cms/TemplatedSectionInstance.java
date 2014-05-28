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

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TemplatedSectionInstance extends TemplatedSectionInstance_Base {

    public TemplatedSectionInstance(TemplatedSection template, Site site) {
        super();
        setSite(site);
        setSectionTemplate(template);
        setOrder(site.getAssociatedSectionSet().size());
    }

    public TemplatedSectionInstance(TemplatedSection template, Section parent) {
        super();
        setParent(parent);
        setSectionTemplate(template);
        setOrder(parent.getChildSet().size());
    }

    @Override
    public MultiLanguageString getName() {
        return getSectionTemplate().getName();
    }

    @Override
    public void setName(MultiLanguageString name) {
        throw new UnsupportedOperationException("Cannot edit templated section name!");
    }

    @Override
    public void delete() {
        setSectionTemplate(null);
        super.delete();
    }

}
