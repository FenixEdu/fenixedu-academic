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

import java.util.Comparator;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class DomainOperationLog extends DomainOperationLog_Base {

    protected static final String SEPARATOR = " - ";
    protected static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";

    public static final Comparator<DomainOperationLog> COMPARATOR_BY_WHEN_DATETIME = new Comparator<DomainOperationLog>() {

        @Override
        public int compare(DomainOperationLog domainOperationLog1, DomainOperationLog domainOperationLog2) {
            final DateTime dateTime1 = domainOperationLog1.getWhenDateTime();
            final DateTime dateTime2 = domainOperationLog2.getWhenDateTime();
            int res = dateTime2.compareTo(dateTime1);
            return res;
        }
    };

    public DomainOperationLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
        this.setPerson(AccessControl.getPerson());
        this.setWhenDateTime(new DateTime());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
