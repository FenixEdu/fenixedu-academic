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

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem {

    protected Boolean run(Site site, final Item item) {
        if (item != null) {
            item.delete();
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteItem serviceInstance = new DeleteItem();

    @Atomic
    public static Boolean runDeleteItem(Site site, Item item) throws NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, item);
    }

}