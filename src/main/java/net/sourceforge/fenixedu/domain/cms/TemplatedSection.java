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

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TemplatedSection extends TemplatedSection_Base {

    public TemplatedSection(SiteTemplate template, MultiLanguageString name, String customPath, boolean visibleInMenus) {
        setTemplate(template);
        setCustomPath(customPath);
        setName(name);
        setOrder(template.getTemplatedSectionSet().size());
        setVisible(visibleInMenus);
    }

    @Override
    public String getCustomPath() {
        return super.getCustomPath();
    }

    @Override
    public void delete() {
        setTemplate(null);
        for (TemplatedSectionInstance instance : getInstanceSet()) {
            instance.delete();
        }
        super.delete();
    }

}
