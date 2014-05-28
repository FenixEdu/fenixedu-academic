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

import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    static public final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new Comparator<ShiftEnrolment>() {
        @Override
        public int compare(ShiftEnrolment o1, ShiftEnrolment o2) {
            final int c = o1.getCreatedOn().compareTo(o2.getCreatedOn());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }
    };

    public ShiftEnrolment(final Shift shift, final Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistration(registration);
        setShift(shift);
        setCreatedOn(new DateTime());
    }

    public void delete() {
        setShift(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean hasRegistration(final Registration registration) {
        return getRegistration() == registration;
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreatedOn() {
        return getCreatedOn() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

}
