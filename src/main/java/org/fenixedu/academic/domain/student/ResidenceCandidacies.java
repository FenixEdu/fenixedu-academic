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
/*
 * Created on Aug 3, 2004
 *
 */
package org.fenixedu.academic.domain.student;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends ResidenceCandidacies_Base {

    public ResidenceCandidacies() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDateDateTime(new DateTime());
    }

    public ResidenceCandidacies(String observations) {
        this();
        setObservations(observations);
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateDateTime(null);
        } else {
            setCreationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
