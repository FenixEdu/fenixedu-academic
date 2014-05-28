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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.cms.CmsContent;

public class ModifiedContentBean {

    private final Section newParent;
    private final CmsContent content;
    private final int order;

    public ModifiedContentBean(Section newParent, CmsContent content, int order) {
        super();
        this.newParent = newParent;
        this.content = content;
        this.order = order;
    }

    public Section getNewParent() {
        return newParent;
    }

    public CmsContent getContent() {
        return content;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return String.valueOf(newParent) + ":" + content.toString() + ":" + order;
    }

}