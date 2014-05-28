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
package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public abstract class AcademicEvent extends AcademicEvent_Base {

    public AcademicEvent() {
        super();
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
        init(eventType, person);
        super.setAdministrativeOffice(administrativeOffice);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException("error.accounting.Event.cannot.modify.administrativeOffice");
    }

    @Override
    public boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        return (!hasAdministrativeOffice() || getAdministrativeOffice() == administrativeOffice);
    }

    @Override
    protected void disconnect() {
        super.setAdministrativeOffice(null);
        super.disconnect();
    }

    public boolean hasAcademicEventExemption() {
        return getAcademicEventExemption() != null;
    }

    public AcademicEventExemption getAcademicEventExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof AcademicEventExemption) {
                return (AcademicEventExemption) exemption;
            }
        }
        return null;
    }

    @Override
    public Unit getOwnerUnit() {
        return getAdministrativeOffice().getUnit();
    }

    @Deprecated
    public boolean hasAdministrativeOffice() {
        return getAdministrativeOffice() != null;
    }

}
