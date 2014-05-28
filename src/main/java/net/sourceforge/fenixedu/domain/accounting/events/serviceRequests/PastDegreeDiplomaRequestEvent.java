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

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastDegreeDiplomaRequestEvent extends PastDegreeDiplomaRequestEvent_Base implements IPastRequestEvent {

    protected PastDegreeDiplomaRequestEvent() {
        super();
    }

    public PastDegreeDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final PastDiplomaRequest pastDiplomaRequest, final Money pastAmount) {
        this();
        super.init(administrativeOffice, EventType.PAST_DEGREE_DIPLOMA_REQUEST, person, pastDiplomaRequest);
        super.setPastAmount(pastAmount);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getEventType().getQualifiedName(), "enum");
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
        return labelFormatter;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.DIPLOMA_REQUEST_FEE);
    }

    @Override
    public void setPastAmount(Money pastDegreeDiplomaRequestAmount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

    @Deprecated
    public boolean hasPastAmount() {
        return getPastAmount() != null;
    }

}
