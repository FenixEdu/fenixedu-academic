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

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This exception is used to signal when the user tries to add a file but the
 * file size exceeds the site quota.
 * 
 * @see Site#getQuota()
 * 
 * @author cfgi
 */
public class SiteFileQuotaExceededException extends DomainException {

    private static final long serialVersionUID = 1L;

    private static String MESSAGE = "site.quota.exceeded";

    public SiteFileQuotaExceededException(Site site, int size) {
        super(MESSAGE, getSizeValue(site.getQuota()));
    }

    private static String getSizeValue(long quota) {
        return String.format("%.2f MB", quota / 1024.0 / 1024.0);
    }

}
