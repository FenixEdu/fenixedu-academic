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
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.EquivalencePlanRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class EquivalencePlanRequestEvent extends EquivalencePlanRequestEvent_Base {

    protected EquivalencePlanRequestEvent() {
        super();
    }

    public EquivalencePlanRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final EquivalencePlanRequest academicServiceRequest) {
        this();
        super.init(administrativeOffice, EventType.EQUIVALENCE_PLAN_REQUEST, person, academicServiceRequest);
    }

    @Override
    public EquivalencePlanRequest getAcademicServiceRequest() {
        return (EquivalencePlanRequest) super.getAcademicServiceRequest();
    }

    private Registration getRegistration() {
        return getAcademicServiceRequest().getRegistration();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel("label.in", Bundle.APPLICATION);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getRegistration().getLastDegreeCurricularPlan().getName());
        labelFormatter.appendLabel(")");
        if (getAcademicServiceRequest().getExecutionYear() != null) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
        }

        return labelFormatter;
    }

    public Integer getNumberOfEquivalences() {
        return getAcademicServiceRequest().getNumberOfEquivalences();
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }
}
