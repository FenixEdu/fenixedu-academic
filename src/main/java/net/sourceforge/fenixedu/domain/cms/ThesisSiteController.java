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

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class ThesisSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        String thesisId = parts[0];
        // If the ID is neither an OID or an IdInternal, simply return null
        if (!StringUtils.isNumeric(thesisId)) {
            return null;
        }
        DomainObject selectedObject = FenixFramework.getDomainObject(thesisId);
        if (selectedObject instanceof Thesis) {
            return ((Thesis) selectedObject).getSite();
        } else {
            // Identifier should be an IdInternal, let's try to process it...
            long oid = 2353642078208l + Integer.parseInt(thesisId);
            Thesis thesis = FenixFramework.getConfig().getBackEnd().fromOid(oid);
            return thesis == null ? null : thesis.getSite();
        }
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ThesisSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
