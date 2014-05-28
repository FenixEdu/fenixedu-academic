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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class SearchDiplomasBySituationParametersBean extends AcademicServiceRequestBean {

    LocalDate searchBegin;

    LocalDate searchEnd;

    public SearchDiplomasBySituationParametersBean(final Person responsible) {
        super(responsible, (String) null);
    }

    public LocalDate getSearchBegin() {
        return searchBegin;
    }

    public void setSearchBegin(LocalDate searchBegin) {
        this.searchBegin = searchBegin;
    }

    public LocalDate getSearchEnd() {
        return searchEnd;
    }

    public void setSearchEnd(LocalDate searchEnd) {
        this.searchEnd = searchEnd;
    }

    @Override
    public Collection<AcademicServiceRequest> searchAcademicServiceRequests() {
        return AcademicAuthorizationGroup.getAcademicServiceRequests(AccessControl.getPerson(), serviceRequestYear, academicServiceRequestSituationType, new Interval(searchBegin.toDateTimeAtStartOfDay(), searchEnd.toDateTimeAtStartOfDay()));
    }

}
