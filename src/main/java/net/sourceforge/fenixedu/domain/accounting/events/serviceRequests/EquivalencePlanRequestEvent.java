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
package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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

        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getRegistration().getLastDegreeCurricularPlan().getName());
        labelFormatter.appendLabel(")");
        if (getAcademicServiceRequest().hasExecutionYear()) {
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
