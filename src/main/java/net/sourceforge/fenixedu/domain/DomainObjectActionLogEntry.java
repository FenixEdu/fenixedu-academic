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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public class DomainObjectActionLogEntry extends DomainObjectActionLogEntry_Base {

    public DomainObjectActionLogEntry(String name, String value, DomainObjectActionLog actionLog) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setName(name);
        setValue(value);
        setDomainObjectActionLog(actionLog);
    }

    @Override
    public void setName(String name) {
        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.domainObjectActionLogEntry.empty.name");
        }
        super.setName(name);
    }

    @Override
    public void setDomainObjectActionLog(DomainObjectActionLog domainObjectActionLog) {
        if (domainObjectActionLog == null) {
            throw new DomainException("error.domainObjectActionLogEntry.empty.domainObjectActionLog");
        }
        super.setDomainObjectActionLog(domainObjectActionLog);
    }

    public String getPresentationNameValue() {
        return getName() + "=" + getValue();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDomainObjectActionLog() {
        return getDomainObjectActionLog() != null;
    }

}
