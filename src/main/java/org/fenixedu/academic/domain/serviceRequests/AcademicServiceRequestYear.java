/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Collection;
import java.util.Collections;

import org.fenixedu.bennu.core.domain.Bennu;

public class AcademicServiceRequestYear extends AcademicServiceRequestYear_Base {
    private AcademicServiceRequestYear(final int year) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        setYear(Integer.valueOf(year));
        setLatestServiceRequestNumber(Integer.valueOf(0));
    }

    static final public AcademicServiceRequestYear readByYear(final int year, boolean create) {
        for (final AcademicServiceRequestYear requestYear : Bennu.getInstance().getAcademicServiceRequestYearsSet()) {
            if (requestYear.getYear().intValue() == year) {
                return requestYear;
            }
        }
        if (create) {
            return new AcademicServiceRequestYear(year);
        }
        return null;
    }

    public static Collection<AcademicServiceRequest> getAcademicServiceRequests(int year) {
        AcademicServiceRequestYear requestYear = readByYear(year, false);
        if (requestYear == null) {
            return Collections.emptySet();
        }
        return requestYear.getAcademicServiceRequestsSet();
    }

    protected Integer generateServiceRequestNumber() {
        setLatestServiceRequestNumber(Integer.valueOf(getLatestServiceRequestNumber().intValue() + 1));
        return getLatestServiceRequestNumber();
    }

}
