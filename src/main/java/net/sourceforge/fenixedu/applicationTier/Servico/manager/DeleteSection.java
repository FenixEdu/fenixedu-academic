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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

/**
 * 
 * @author lmac1
 */

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixframework.Atomic;

public class DeleteSection {

    protected Boolean run(Site site, final Section section) {
        if (section != null) {
            section.delete();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteSection serviceInstance = new DeleteSection();

    @Atomic
    public static Boolean runDeleteSection(Site site, Section section) throws NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, section);
    }

}