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
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;

public class BolonhaAdvancedFormationDiplomaRequestEvent extends BolonhaAdvancedFormationDiplomaRequestEvent_Base {

    protected BolonhaAdvancedFormationDiplomaRequestEvent() {
        super();
    }

    public BolonhaAdvancedFormationDiplomaRequestEvent(final AdministrativeOffice administrativeOffice,
            final EventType eventType, final Person person, final DiplomaRequest diplomaRequest) {
        this();
        super.init(administrativeOffice, eventType, person, diplomaRequest);
    }

}
